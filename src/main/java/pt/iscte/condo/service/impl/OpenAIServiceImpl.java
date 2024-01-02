package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.iscte.condo.enums.OpenAIRole;
import pt.iscte.condo.proxy.OpenAI;
import pt.iscte.condo.proxy.dto.Content;
import pt.iscte.condo.proxy.dto.ThreadMessage;
import pt.iscte.condo.proxy.request.AddMessageRequest;
import pt.iscte.condo.proxy.request.RunThreadRequest;
import pt.iscte.condo.proxy.responses.GetMessagesResponse;
import pt.iscte.condo.proxy.responses.GetThreadStatusResponse;
import pt.iscte.condo.service.OpenAIService;
import pt.iscte.condo.utils.RateLimitedQueue;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.assistant}")
    private String assistantId;

    private final OpenAI openAI;
    private final RateLimitedQueue<CompletableFuture<String>> queue;

    @Override
    public CompletableFuture<String> createNewThread(String transcript) {

        CompletableFuture<String> futureThreadId = new CompletableFuture<>();
        CompletableFuture<String> futureRunId = new CompletableFuture<>();
        CompletableFuture<GetThreadStatusResponse> futureThreadStatus = new CompletableFuture<>();
        CompletableFuture<GetThreadStatusResponse> newFutureThreadStatus = new CompletableFuture<>();

        Callable<CompletableFuture<String>> task = () -> {

            // create thread
            queue.schedule(() -> {
                String threadId = openAI.createThread().getId(); //todo check if thread is null and http status code
                System.out.println("threadId: " + threadId);
                futureThreadId.complete(threadId);
                return null;
            });

            // add message to thread
            futureThreadId.thenAccept(threadId -> {
                AddMessageRequest addMessageRequest = AddMessageRequest.builder()
                        .content(transcript)
                        .role(OpenAIRole.user.toString())
                        .build();

                queue.schedule(() -> {
                    openAI.addMessage(threadId, addMessageRequest); //todo check http status code
                    System.out.println("addMessageRequest: " + addMessageRequest);
                    return null;
                });
            });

            // run thread
            futureThreadId.thenAccept(threadId -> {
                RunThreadRequest runThreadRequest = RunThreadRequest.builder()
                        .assistant_id(assistantId)
                        .build();

                queue.schedule(() -> {
                    String runId = openAI.runThread(threadId, runThreadRequest).getId(); //todo check http status code
                    System.out.println("runThreadRequest: " + runThreadRequest);
                    futureRunId.complete(runId);
                    return null;
                });

            });

            futureThreadId.thenCombine(futureRunId, (threadId, runId) -> {
                queue.schedule(() -> {
                    GetThreadStatusResponse threadStatusResponse = openAI.getThreadStatus(threadId, runId); //todo check http status code
                    System.out.println("threadStatus: " + threadStatusResponse.getStatus());
                    futureThreadStatus.complete(threadStatusResponse);
                    return null;
                });

                // run while chat assistant thread is not completed
                futureThreadStatus.thenAccept(threadStatusResponse -> {
                    while (!Objects.equals(threadStatusResponse.getStatus(), "completed")) {
                        queue.schedule(() -> {
                            GetThreadStatusResponse newThreadStatusResponse = openAI.getThreadStatus(threadId, runId); //todo check http status code
                            System.out.println("new threadStatus: " + newThreadStatusResponse.getStatus());
                            newFutureThreadStatus.complete(newThreadStatusResponse);
                            return null;
                        });
                        try {
                            threadStatusResponse = newFutureThreadStatus.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                return null;
            });
            return null;
        };

        queue.schedule(task);

        return futureThreadId;
    }

    @Override
    public CompletableFuture<String> askAssistant(String threadId, String question) {

        CompletableFuture<String> futureRunId = new CompletableFuture<>();
        CompletableFuture<GetThreadStatusResponse> futureThreadStatus = new CompletableFuture<>();
        CompletableFuture<GetThreadStatusResponse> newFutureThreadStatus = new CompletableFuture<>();
        CompletableFuture<GetMessagesResponse> futureAnswerResponse = new CompletableFuture<>();
        CompletableFuture<String> futureAnswer = new CompletableFuture<>();

        Callable<CompletableFuture<String>> task = () -> {

            AddMessageRequest addMessageRequest = AddMessageRequest.builder()
                    .content(question)
                    .role(OpenAIRole.user.toString())
                    .build();

            queue.schedule(() -> {
                openAI.addMessage(threadId, addMessageRequest); //todo check http status code
                System.out.println("addMessageRequest: " + addMessageRequest.getContent());
                return null;
            });

            // run thread
            RunThreadRequest runThreadRequest = RunThreadRequest.builder()
                    .assistant_id(assistantId)
                    .build();

            queue.schedule(() -> {
                String runId = openAI.runThread(threadId, runThreadRequest).getId(); //todo check http status code
                futureRunId.complete(runId);
                return null;
            });

            futureRunId.thenAccept(runId -> {
                queue.schedule(() -> {
                    GetThreadStatusResponse response = openAI.getThreadStatus(threadId, runId); //todo check http status code
                    System.out.println("threadStatusMessage: " + response.getStatus());
                    futureThreadStatus.complete(response);
                    return null;
                });

                // run while chat assistant thread is not completed
                futureThreadStatus.thenAccept(threadStatusResponse -> {
                    while (!Objects.equals(threadStatusResponse.getStatus(), "completed")) {
                        queue.schedule(() -> {
                            GetThreadStatusResponse response = openAI.getThreadStatus(threadId, runId); //todo check http status code
                            System.out.println("new threadStatusMessage: " + response.getStatus());
                            newFutureThreadStatus.complete(response);
                            return null;
                        });
                        try {
                            threadStatusResponse = newFutureThreadStatus.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            });

            queue.schedule(() -> {
                GetMessagesResponse response = openAI.getMessages(threadId); //todo check http status code
                System.out.println("message firstId: " + response.getFirst_id());
                futureAnswerResponse.complete(response);
                return null;
            });

            futureAnswerResponse.thenAccept(response -> {
                String answer = getAnswerById(response.getData(), response.getFirst_id());
                futureAnswer.complete(answer);
            });

            return null;
        };
        queue.schedule(task);
        return futureAnswer;
    }

    private String getAnswerById(List<ThreadMessage> messages, String id) {
        for (ThreadMessage message : messages) {
            if (message.getId().equals(id)) {
                for (Content content : message.getContent()) {
                    return content.getText().getValue();
                }
            }
        }
        return null;
    }

}

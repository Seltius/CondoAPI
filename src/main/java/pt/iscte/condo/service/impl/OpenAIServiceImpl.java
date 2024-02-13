package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.iscte.condo.enums.OpenAIRole;
import pt.iscte.condo.proxy.OpenAI;
import pt.iscte.condo.proxy.dto.Content;
import pt.iscte.condo.proxy.dto.ThreadMessage;
import pt.iscte.condo.proxy.dto.request.AddMessageRequest;
import pt.iscte.condo.proxy.dto.request.RunThreadRequest;
import pt.iscte.condo.proxy.dto.response.GetMessagesResponse;
import pt.iscte.condo.proxy.dto.response.GetThreadStatusResponse;
import pt.iscte.condo.service.OpenAIService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.assistant}")
    private String assistantId;

    private final OpenAI openAI;

    @Override
    public String createNewThread(String transcript) {

        // create thread
        String threadId = openAI.createThread().getId(); //todo check if thread is null and http status code
        System.out.println("threadId: " + threadId);

        // add message to thread
        AddMessageRequest addMessageRequest = AddMessageRequest.builder()
                .content(transcript)
                .role(OpenAIRole.user.toString())
                .build();

        openAI.addMessage(threadId, addMessageRequest); //todo check http status code
        System.out.println("addMessageRequest: " + addMessageRequest);

        // run thread
        RunThreadRequest runThreadRequest = RunThreadRequest.builder()
                .assistant_id(assistantId)
                .build();

        String runId = openAI.runThread(threadId, runThreadRequest).getId(); //todo check http status code
        System.out.println("runThreadRequest: " + runThreadRequest);

        GetThreadStatusResponse threadStatus = openAI.getThreadStatus(threadId, runId); //todo check http status code
        System.out.println("threadStatus: " + threadStatus.getStatus());

        checkThreadStatus(threadId, runThreadRequest, runId, threadStatus);

        return threadId;
    }

    @Override
    public String askAssistant(String threadId, String question) {

        // add message to thread
        AddMessageRequest addMessageRequest = AddMessageRequest.builder()
                .content(question)
                .role(OpenAIRole.user.toString())
                .build();

        openAI.addMessage(threadId, addMessageRequest); //todo check http status code
        System.out.println("addMessageRequest: " + addMessageRequest.getContent());

        // run thread
        RunThreadRequest runThreadRequest = RunThreadRequest.builder()
                .assistant_id(assistantId)
                .build();

        String runId = openAI.runThread(threadId, runThreadRequest).getId(); //todo check http status code

        GetThreadStatusResponse threadStatus = openAI.getThreadStatus(threadId, runId); //todo check http status code
        System.out.println("threadStatusMessage: " + threadStatus.getStatus());

        // run while chat assistant thread is not completed
        checkThreadStatus(threadId, runThreadRequest, runId, threadStatus);

        GetMessagesResponse getMessagesResponse = openAI.getMessages(threadId); //todo check http status code
        System.out.println("message firstId: " + getMessagesResponse.getFirst_id());

        return getAnswerById(getMessagesResponse.getData(), getMessagesResponse.getFirst_id());
    }

    private void checkThreadStatus(String threadId, RunThreadRequest runThreadRequest, String runId, GetThreadStatusResponse threadStatus) {
        while (!Objects.equals(threadStatus.getStatus(), "completed")) {
            threadStatus = openAI.getThreadStatus(threadId, runId); //todo check http status code
            System.out.println("runId: " + runId);
            System.out.println("threadStatus: " + threadStatus.getStatus());

            if (Objects.equals(threadStatus.getStatus(), "failed")) {
                runId = openAI.runThread(threadId, runThreadRequest).getId();
                try {
                    Thread.sleep(20000); // pause for 20 second //todo execute this procedure in a different thread
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(1000); // pause for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

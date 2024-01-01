package pt.iscte.condo.proxy;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pt.iscte.condo.config.OpenAIConfig;
import pt.iscte.condo.proxy.request.*;
import pt.iscte.condo.proxy.responses.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "openai", url = "https://api.openai.com/v1/", configuration = OpenAIConfig.class)
@Headers("OpenAI-Beta: assistants=v1")
public interface OpenAI {

    @PostMapping(value = "assistants", consumes = APPLICATION_JSON_VALUE)
    CreateAssistantResponse createAssistant(CreateAssistantRequest request);

    @PostMapping(value = "threads", consumes = APPLICATION_JSON_VALUE)
    CreateThreadResponse createThread();

    @PostMapping(value = "threads/{threadId}/messages", consumes = APPLICATION_JSON_VALUE)
    AddMessageResponse addMessage(@PathVariable("threadId") String threadId, AddMessageRequest request);

    @PostMapping(value = "threads/{threadId}/runs", consumes = APPLICATION_JSON_VALUE)
    RunThreadResponse runThread(@PathVariable("threadId") String threadId, RunThreadRequest request);

    @GetMapping(value = "threads/{threadId}/runs/{runId}", consumes = APPLICATION_JSON_VALUE)
    GetThreadStatusResponse getThreadStatus(@PathVariable("threadId") String threadId, @PathVariable("runId") String runId);

    @GetMapping(value = "threads/{threadId}/messages", consumes = APPLICATION_JSON_VALUE)
    GetMessagesResponse getMessages(@PathVariable("threadId") String threadId);

}

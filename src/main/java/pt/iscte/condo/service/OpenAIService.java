package pt.iscte.condo.service;

import java.util.concurrent.CompletableFuture;

public interface OpenAIService {

    CompletableFuture<String> createNewThread(String transcript);

    CompletableFuture<String> askAssistant(String threadId, String question);

}

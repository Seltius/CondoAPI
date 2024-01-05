package pt.iscte.condo.service;

public interface OpenAIService {

    String createNewThread(String transcript);

    String askAssistant(String threadId, String question);

}

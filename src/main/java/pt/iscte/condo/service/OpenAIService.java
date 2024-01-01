package pt.iscte.condo.service;

public interface OpenAIService {

    String startAssistant(String transcript);

    String runAssistant(String threadId, String question);

}

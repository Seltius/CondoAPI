package pt.iscte.condo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.iscte.condo.enums.TranscriptStatus;
import pt.iscte.condo.repository.AIAnswerRepository;
import pt.iscte.condo.repository.TranscriptRepository;
import pt.iscte.condo.repository.entities.AIAnswer;
import pt.iscte.condo.repository.entities.MeetingTopic;
import pt.iscte.condo.repository.entities.Transcript;
import pt.iscte.condo.service.OpenAIService;
import pt.iscte.condo.service.TranscriptService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranscriptServiceImpl implements TranscriptService {

    // Services
    private final OpenAIService openAIService;

    // Repositories
    private final AIAnswerRepository aiAnswerRepository;
    private final TranscriptRepository transcriptRepository;

    @Transactional
    public Map<String, String> processTranscript(Transcript transcript, List<MeetingTopic> meetingTopics) {
        Map<String, String> answers = new HashMap<>();

        String threadId = openAIService.createNewThread(transcript.getText());

        meetingTopics.forEach(
                question -> {
                    processQuestion(threadId, question, transcript, answers);
                }
        );

        transcript.setStatus(TranscriptStatus.COMPLETED);
        transcriptRepository.save(transcript);

        return answers;
    }

    private void processQuestion(String threadId, MeetingTopic topic, Transcript transcript, Map<String, String> answers) {
        String answer = openAIService.askAssistant(threadId, topic.getTopic());
        AIAnswer aiAnswer = AIAnswer.builder()
                .answer(answer)
                .topic(topic)
                .transcript(transcript)
                .build();
        aiAnswerRepository.save(aiAnswer);
        answers.put(topic.getTopic(), answer);
    }

}
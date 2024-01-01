package pt.iscte.condo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.iscte.condo.domain.AIAnswer;
import pt.iscte.condo.domain.AIQuestion;
import pt.iscte.condo.domain.MeetingTranscript;
import pt.iscte.condo.enums.TranscriptStatus;
import pt.iscte.condo.repository.AIAnswerRepository;
import pt.iscte.condo.repository.AIQuestionRepository;
import pt.iscte.condo.repository.MeetingTranscriptRepository;
import pt.iscte.condo.service.OpenAIService;
import pt.iscte.condo.service.TranscriptService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscriptServiceImpl implements TranscriptService {

    private final OpenAIService openAIService;
    private final AIQuestionRepository aiQuestionsRepository;
    private final AIAnswerRepository aiAnswerRepository;
    private final MeetingTranscriptRepository transcriptRepository;

    @Override
    @Scheduled(fixedRate = 600000) // 600000 milliseconds = 10 minutes
    @Transactional
    public void processTranscripts() {

        List<AIQuestion> questions = aiQuestionsRepository.findAll();
        transcriptRepository.findAllByStatus(TranscriptStatus.TO_PROCESS).ifPresent(transcripts -> {

            for (MeetingTranscript transcript : transcripts) {
                String threadId = openAIService.startAssistant(transcript.getTranscript());

                for (AIQuestion question : questions) {

                    String answer = openAIService.runAssistant(threadId, question.getQuestion());

                    AIAnswer aiAnswer = AIAnswer.builder()
                            .answer(answer)
                            .question(question)
                            .transcript(transcript)
                            .build();

                    aiAnswerRepository.save(aiAnswer);

                }

                //todo generate minute pdf

                transcript.setStatus(TranscriptStatus.COMPLETED);
                transcriptRepository.save(transcript);

            }
        });
    }
}
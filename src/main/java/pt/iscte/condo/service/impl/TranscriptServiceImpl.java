package pt.iscte.condo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.domain.*;
import pt.iscte.condo.enums.DocumentType;
import pt.iscte.condo.enums.TranscriptStatus;
import pt.iscte.condo.repository.*;
import pt.iscte.condo.service.OpenAIService;
import pt.iscte.condo.service.PdfService;
import pt.iscte.condo.service.TranscriptService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TranscriptServiceImpl implements TranscriptService {

    // Services
    private final OpenAIService openAIService;
    private final AIQuestionRepository aiQuestionsRepository;
    private final PdfService pdfService;

    // Repositories
    private final AIAnswerRepository aiAnswerRepository;
    private final MeetingTranscriptRepository transcriptRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository; // TODO DELETE (JUST FOR TEST PURPOSES)
    private final CondominiumRepository condominiumRepository; // TODO DELETE (JUST FOR TEST PURPOSES)

    @Override
    //@Scheduled(fixedRate = 600000) // 600000 milliseconds = 10 minutes
    @Transactional
    public void processTranscripts() {

        List<AIQuestion> questions = aiQuestionsRepository.findAll();
        Map<String, String> answers = new HashMap<>();

        transcriptRepository.findAllByStatus(TranscriptStatus.TO_PROCESS).ifPresent(transcripts -> {

            for (MeetingTranscript transcript : transcripts) {
                CompletableFuture<String> futureThreadId = openAIService.createNewThread(transcript.getTranscript());
                System.out.println("futureThreadId: " + futureThreadId);

                futureThreadId.thenAccept(threadId -> {

                    CompletableFuture[] futures = questions
                            .stream()
                            .map(question -> {
                                CompletableFuture<String> futureAnswer = openAIService.askAssistant(threadId, question.getQuestion());
                                System.out.println("futureAnswer: " + futureAnswer);

                                    return futureAnswer.thenAccept(answer -> {
                                        answers.put(question.getPlaceholder(), answer);

                                        AIAnswer aiAnswer = AIAnswer.builder()
                                                .answer(answer)
                                                .question(question)
                                                .transcript(transcript)
                                                .build();

                                        aiAnswerRepository.save(aiAnswer);

                                    });
                            }).toArray(CompletableFuture[]::new);

                    CompletableFuture.allOf(futures).thenRun(() -> {

                        try {
                            //TODO DELETE (JUST FOR TEST PURPOSES
                            User user = userRepository.findById(1)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

                            Condominium condominium = condominiumRepository.findById(1)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condominium not found"))  ;

                            Document document = Document.builder()
                                    .name("TEST.pdf") //TODO GENERATE PROGRAMMATIC
                                    .uploader(user) //TODO GENERATE PROGRAMMATIC
                                    .user(user) //TODO GENERATE PROGRAMMATIC
                                    .uploadDate(LocalDateTime.now())
                                    .fileData(pdfService.generateMinute(answers))
                                    .type(DocumentType.MINUTES)
                                    .condominium(condominium) //TODO GENERATE PROGRAMMATIC
                                    .build();

                            documentRepository.save(document);
                        } catch (Exception e) {
                            throw new RuntimeException("PDF File generation failed", e.getCause()); //todo recover ? log ?
                        }

                        transcript.setStatus(TranscriptStatus.COMPLETED);
                        transcriptRepository.save(transcript);
                    });
                });
            }
        });
    }
}
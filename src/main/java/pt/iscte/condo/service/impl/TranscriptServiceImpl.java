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

@Service
@RequiredArgsConstructor
public class TranscriptServiceImpl implements TranscriptService {

    // Services
    private final OpenAIService openAIService;
    private final AIQuestionRepository aiQuestionsRepository;
    private final PdfService pdfService;

    // Repositories
    private final AIAnswerRepository aiAnswerRepository;
    private final TranscriptRepository transcriptRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository; // TODO DELETE (JUST FOR TEST PURPOSES)
    private final CondominiumRepository condominiumRepository; // TODO DELETE (JUST FOR TEST PURPOSES)

    @Override
    //@Scheduled(fixedRate = 600000) // 600000 milliseconds = 10 minutes
    @Transactional
    public void processTranscripts() {

        List<AIQuestion> questions = aiQuestionsRepository.findAll();
        Map<String, String> answers = new HashMap<>();

        transcriptRepository.findAllByStatus(TranscriptStatus.TO_PROCESS).ifPresent(transcripts ->
                transcripts.parallelStream().forEach(transcript -> handleTranscript(transcript, questions, answers)));
    }

    @Transactional
    public Map<String, String> processTranscript(Transcript transcript) {

        List<AIQuestion> questions = aiQuestionsRepository.findAll();
        Map<String, String> answers = new HashMap<>();
        Map<String, String> output = new HashMap<>();

        String threadId = openAIService.createNewThread(transcript.getText());

        questions.forEach(
                question -> {
                    AIAnswer aiAnswer = processQuestion(threadId, question, transcript, answers);
                    output.put(aiAnswer.getId().toString(), aiAnswer.getAnswer());
                }
        );

        transcript.setStatus(TranscriptStatus.COMPLETED);
        transcriptRepository.save(transcript);

        return output;
    }

    private void handleTranscript(Transcript transcript, List<AIQuestion> questions, Map<String, String> answers) {

        String threadId = openAIService.createNewThread(transcript.getText());

        questions.forEach(
                question -> processQuestion(threadId, question, transcript, answers)
        );

        finalizeTranscripts(transcript, answers);
    }

    private AIAnswer processQuestion(String threadId, AIQuestion question, Transcript transcript, Map<String, String> answers) {
        String answer = openAIService.askAssistant(threadId, question.getQuestion());
        answers.put(question.getPlaceholder(), answer);

        AIAnswer aiAnswer = AIAnswer.builder()
                .answer(answer)
                .question(question)
                .transcript(transcript)
                .build();

            return aiAnswerRepository.saveAndFlush(aiAnswer);
    }

    private void finalizeTranscripts(Transcript transcript, Map<String, String> answers) {

        try {
            byte[] pdfData = pdfService.generateMinute(answers);
            saveDocument(pdfData);

            transcript.setStatus(TranscriptStatus.COMPLETED);
            transcriptRepository.save(transcript);
        } catch (Exception e) {
            throw new RuntimeException("PDF File generation failed", e.getCause()); //todo recover ? log ?
        }

    }

    private void saveDocument(byte[] pdfData) {

        User user = userRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Condominium condominium = condominiumRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condominium not found"));

        Document document = Document.builder()
                .name("TEST.pdf") //TODO GENERATE PROGRAMMATIC
                .uploader(user) //TODO GENERATE PROGRAMMATIC
                .user(user) //TODO GENERATE PROGRAMMATIC
                .uploadDate(LocalDateTime.now())
                .fileData(pdfData)
                .type(DocumentType.MINUTE)
                .condominium(condominium) //TODO GENERATE PROGRAMMATIC
                .build();

        documentRepository.save(document);

    }

}
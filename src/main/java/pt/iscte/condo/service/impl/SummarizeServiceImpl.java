package pt.iscte.condo.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.domain.Condominium;
import pt.iscte.condo.domain.Document;
import pt.iscte.condo.domain.Transcript;
import pt.iscte.condo.enums.TranscriptStatus;
import pt.iscte.condo.mapper.DocumentMapper;
import pt.iscte.condo.repository.CondominiumUserRepository;
import pt.iscte.condo.repository.TranscriptRepository;
import pt.iscte.condo.service.SummarizeService;
import pt.iscte.condo.service.TranscriptService;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SummarizeServiceImpl implements SummarizeService {

    private final TranscriptService transcriptService;

    private final DocumentMapper documentMapper;

    private final CondominiumUserRepository condominiumUserRepository;
    private final TranscriptRepository transcriptRepository;

    @Override
    public Map<String, String> summarizeDocument(DocumentRequest request) {

        Document document = documentMapper.documentRequestToDocument(request);
        byte[] fileData = document.getFileData();
        String fileType = new Tika().detect(document.getFileData());

        if (!fileType.equals("text/plain")) {
            throw new IllegalArgumentException("File must be a text file");
        }

        // CHECK IF DOCUMENT IS EMPTY
        if (fileData.length == 0) {
            throw new IllegalArgumentException("File must not be empty");
        }

        // CONVERT BYTES TO STRING
        String text = new String(fileData, StandardCharsets.UTF_8);

        return transcriptService.processTranscript(getTranscript(text, request.getOwnerId()));

    }

    private Transcript getTranscript(String text, Integer userId) {

        Condominium condominium = condominiumUserRepository.findById(userId).get().getApartment().getCondominium();

        Transcript transcript = Transcript.builder()
                .condominium(condominium)
                .text(text)
                .status(TranscriptStatus.TO_PROCESS)
                .build();

        return transcriptRepository.save(transcript);

    }

}

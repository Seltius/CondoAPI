package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.controller.dto.request.SummarizeRequest;
import pt.iscte.condo.enums.DocumentType;
import pt.iscte.condo.enums.TranscriptStatus;
import pt.iscte.condo.repository.DocumentRepository;
import pt.iscte.condo.repository.MeetingRepository;
import pt.iscte.condo.repository.TranscriptRepository;
import pt.iscte.condo.repository.entities.*;
import pt.iscte.condo.service.PdfService;
import pt.iscte.condo.service.SummarizeService;
import pt.iscte.condo.service.TranscriptService;
import pt.iscte.condo.service.dto.FractionInfo;
import pt.iscte.condo.service.mapper.SummarizeMapper;
import pt.iscte.condo.utils.DocumentUtils;
import pt.iscte.condo.utils.UserUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SummarizeServiceImpl implements SummarizeService {

    // REPOSITORY
    private final TranscriptRepository transcriptRepository;
    private final MeetingRepository meetingRepository;
    private final DocumentRepository documentRepository;

    // UTIL
    private final UserUtils userUtils;
    private final DocumentUtils documentUtils;
    private final SummarizeMapper summarizeMapper;

    // SERVICE
    private final TranscriptService transcriptService;
    private final PdfService pdfService;

    @Override
    public void summarizeDocument(SummarizeRequest request) {
        User user = userUtils.getUserByBearer();
        Document document = getDocument(request, user);
        Meeting meeting = meetingRepository.findById(request.getMeetingId())
                .orElseThrow(() -> new NoSuchElementException("Meeting not found"));
        List<MeetingTopic> meetingTopics = meeting.getTopics();
        Condominium condominium = user.getCondominium();
        List<Topic> topics = summarizeMapper.mapTopicEntityList2Topic(meetingTopics);
        List<FractionInfo> fractionInfoList = summarizeMapper.mapApartmentEntity2Apartment(condominium.getApartments());
        Map<String, byte[]> meetingAttachments = mapAttachments(meeting.getAttachments());

        validateDocument(document);

        // GET TEXT FROM FILE
        String text = new String(document.getFileData(), StandardCharsets.UTF_8);

        Map<String,String> meetingTopicsDescription = transcriptService.processTranscript(getTranscript(text, condominium), meetingTopics);
        // todo topic entity doesnt have stored ai answers
        try {
            byte[] pdfData = pdfService.generateMinute(topics, fractionInfoList, meetingTopicsDescription, condominium, meeting, meetingAttachments);
            saveDocument(pdfData, user, user.getCondominium());
        } catch (Exception e) {
            throw new RuntimeException("PDF File generation failed", e.getCause());
        }

    }

    private void saveDocument(byte[] pdfData, User user, Condominium condominium) {

        Document document = Document.builder()
                .name("Condominium_Minute_" + condominium.getName())
                .uploader(user)
                .user(user)
                .fileData(pdfData)
                .type(DocumentType.MINUTE)
                .condominium(condominium)
                .build();

        documentRepository.save(document);

    }

    private Map<String, byte[]> mapAttachments(List<MeetingAttachment> attachments) {
        Map<String, byte[]> fileMap = new HashMap<>();
        if (attachments != null) {
            for (MeetingAttachment attachment : attachments) {
                fileMap.put(attachment.getName(), attachment.getFileData());
            }
        }
        return fileMap;
    }

    private void validateDocument(Document document) {
        byte[] fileData = document.getFileData();
        String fileType = new Tika().detect(fileData);

        // CHECK IF FILE IS A TEXT FILE
        if (!fileType.equals("text/plain")) {
            throw new IllegalArgumentException("File must be a text file");
        }

        // CHECK IF DOCUMENT IS EMPTY
        if (fileData.length == 0) {
            throw new IllegalArgumentException("File must not be empty");
        }
    }

    private Document getDocument(SummarizeRequest request, User user) {
        Document document = new Document();
        document.setName(request.getTranscriptName());
        document.setType(DocumentType.TRANSCRIPT);
        document.setUser(user);
        document.setUploader(user);
        document.setCondominium(user.getCondominium());
        document.setFileData(documentUtils.stringToBytes(request.getFileData()));
        return document;
    }

    private Transcript getTranscript(String text, Condominium condominium) {
        Transcript transcript = Transcript.builder()
                .condominium(condominium)
                .text(text)
                .status(TranscriptStatus.TO_PROCESS)
                .build();
        return transcriptRepository.save(transcript);
    }

}

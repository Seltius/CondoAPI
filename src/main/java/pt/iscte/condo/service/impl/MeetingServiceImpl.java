package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.controller.dto.Attachment;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.controller.dto.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.dto.request.CreateMeetingRequest;
import pt.iscte.condo.repository.*;
import pt.iscte.condo.repository.entities.*;
import pt.iscte.condo.service.MeetingService;
import pt.iscte.condo.service.PdfService;
import pt.iscte.condo.service.mapper.MeetingMapper;
import pt.iscte.condo.utils.UserUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pt.iscte.condo.enums.DocumentType.MEETING_NOTICE;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final PdfService pdfService;
    private final DocumentRepository documentRepository;
    private final MeetingTopicRepository meetingTopicRepository;

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingAttachmentRepository meetingAttachmentRepository;

    private final UserUtils userUtils;

    private final MeetingMapper meetingMapper;

    @Override
    public void createMeeting(CreateMeetingRequest request) throws Exception {
        //List<User> users = userRepository.findAllById(request.getUserIds());
        User organizer = userUtils.getUserByBearer();
        User secretary = userRepository.findById(request.getSecretaryId()).orElse(null);
        Meeting meeting = storeMeeting(organizer, secretary, request);
        Map<String, byte[]> attachments = mapAttachments(request.getAttachments());
        byte[] meetingNotice = pdfService.generateMeetingNotice(organizer.getCondominium(), meeting, request.getTopics(), attachments);

        if (!attachments.isEmpty())
            storeAttachment(meeting, request.getAttachments());

        storeTopics(request.getTopics(), meeting);
        storeDocument(meetingNotice, meeting, organizer);

        // TODO
        /* for (User user : users) {
            Map<String, Object> registrant = new HashMap<>();
            registrant.put("email", user.getEmail());
            registrant.put("first_name", user.getFirstName());
            registrant.put("last_name", user.getLastName());

            zoomAPI.addRegistrant(meeting.getZoomMeetingId(), registrant, "Bearer " + token);
        } **/

    }

    private void storeAttachment(Meeting meeting, List<Attachment> attachments) {
        for (Attachment attachment : attachments) {
            byte[] decodedFileData = Base64.getDecoder().decode(attachment.getFileData());
            MeetingAttachment attachment2Store = MeetingAttachment.builder()
                    .name(attachment.getName())
                    .fileData(decodedFileData)
                    .meeting(meeting)
                    .build();
            meetingAttachmentRepository.save(attachment2Store);
        }
    }

    private Map<String, byte[]> mapAttachments(List<Attachment> attachments) {
        Map<String, byte[]> fileMap = new HashMap<>();
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                String filename = attachment.getName();
                byte[] decodedContent = Base64.getDecoder().decode(attachment.getFileData());
                fileMap.put(filename, decodedContent);
            }
        }
        return fileMap;
    }

    private void storeTopics(List<Topic> topics, Meeting meeting) {
        for (Topic topic : topics) {
            MeetingTopic meetingTopic = MeetingTopic.builder()
                    .topic(topic.getTopic())
                    .description(topic.getDescription())
                    .meeting(meeting)
                    .build();
            meetingTopicRepository.save(meetingTopic);
        }
    }

    private Meeting storeMeeting(User organizer, User secretary, CreateMeetingRequest request) {

        Meeting meeting = Meeting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .extraMeetingDate(request.getExtraDate())
                .extraStartTime(request.getExtraStartTime())
                .extraEndTime(request.getExtraEndTime())
                .link("https://www.zoom.com/8lkj3##1jkl##n1kj1nk1j") //todo get from response
                .accessId(Long.valueOf("6125371537615")) //todo get from response
                .password("KLASJK@82iuh#molk") //todo get from response
                .organizer(organizer)
                .secretary(secretary)
                .condominium(organizer.getCondominium())
                .topics(meetingMapper.topics2MeetingTopics(request.getTopics())) //todo this is not working
                .build();

        return meetingRepository.save(meeting);
    }

    private void storeDocument(byte[] meetingNotice, Meeting meeting, User organizer) {
        Document meetingNoticeDocument = Document.builder()
                .name("MeetingNotice_" + meeting.getId() + ".pdf")
                .type(MEETING_NOTICE)
                .fileData(meetingNotice)
                .uploader(organizer)
                .user(organizer)
                .condominium(organizer.getCondominium())
                .build();
        documentRepository.save(meetingNoticeDocument);
    }

    @Override
    public List<Meeting> getAllMeetings() {
        User user = userUtils.getUserByBearer();
        return meetingRepository.findAllByCondominium(user.getCondominium());
    }

    @Override
    public void addUsersToMeeting(AddUsersToMeetingRequest request, Integer meetingId) {

        //todo check if user is admin of condominium
        //todo check if users are in condominium

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found"));

        //meeting.getUsers().add(null); //todo relation between meeting and user

        meetingRepository.save(meeting);

    }

}

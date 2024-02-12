package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.config.TokenCacheConfig;
import pt.iscte.condo.controller.dto.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.dto.request.CreateMeetingRequest;
import pt.iscte.condo.domain.Meeting;
import pt.iscte.condo.domain.User;
import pt.iscte.condo.mapper.MeetingMapper;
import pt.iscte.condo.proxy.ZoomAPI;
import pt.iscte.condo.repository.MeetingRepository;
import pt.iscte.condo.repository.UserRepository;
import pt.iscte.condo.service.MeetingService;
import pt.iscte.condo.utils.UserUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    private final ZoomAPI zoomAPI;

    private final TokenCacheConfig tokenCacheConfig;

    private final UserUtils userUtils;

    private final MeetingMapper meetingMapper;

    @Override
    public void createMeeting(CreateMeetingRequest request) {
        /*List<User> users = userRepository.findAllById(request.getUserIds());
        String token = tokenCacheConfig.getToken("zoomToken", tokenCacheConfig.tokenCache());

         MeetingRequest zoomRequest = MeetingRequest.builder()
                .topic(request.getTitle())
                .start_time(request.getDate())
                .duration(request.getDuration())
                .build();

        Map<String, Object> response = zoomAPI.createMeeting("joaodantas94@gmail.com", zoomRequest, "Bearer " + token); //todo remove hardcoded values
        todo **/

        // Get Organizer
        User organizer = userUtils.getUserByBearer();

        // Get Secretary
        User secretary = userRepository.findById(request.getSecretaryId()).orElse(null);

        Meeting meeting = Meeting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .zoomLink("https://localhost.pt") //todo get from response
                .zoomMeetingId(Long.valueOf("6125371537615")) //todo get from response
                .zoomPassword("KLASJK@82iuh#molk") //todo get from response
                .organizer(organizer)
                .secretary(secretary)
                .condominium(organizer.getCondominium())
                .topics(meetingMapper.topics2MeetingTopics(request.getTopics()))
                .build();

        /* for (User user : users) {
            Map<String, Object> registrant = new HashMap<>();
            registrant.put("email", user.getEmail());
            registrant.put("first_name", user.getFirstName());
            registrant.put("last_name", user.getLastName());

            zoomAPI.addRegistrant(meeting.getZoomMeetingId(), registrant, "Bearer " + token); //todo move Bearer to proxy side
        } **/ //todo Only available for paid users (PRO ACCOUNT)

        meetingRepository.save(meeting);

    }

    @Override
    public List<Meeting> getAllMeetings() {

        User user = userUtils.getUserByBearer();
        //todo get condominiumUser relation
        //todo get condominiumId from condominiumUser relation
        //todo abstract to private method

        return meetingRepository.findAllByCondominiumId(1); //todo get user condominium id

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

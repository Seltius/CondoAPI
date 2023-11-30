package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.config.TokenCacheConfig;
import pt.iscte.condo.controller.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.request.CreateMeetingRequest;
import pt.iscte.condo.domain.Meeting;
import pt.iscte.condo.domain.User;
import pt.iscte.condo.proxy.ZoomAPI;
import pt.iscte.condo.proxy.request.MeetingRequest;
import pt.iscte.condo.repository.MeetingRepository;
import pt.iscte.condo.repository.UserRepository;
import pt.iscte.condo.service.ZoomService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoomServiceImpl implements ZoomService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final ZoomAPI zoomAPI;
    private final TokenCacheConfig tokenCacheConfig;

    @Override
    public void createMeeting(CreateMeetingRequest request) {
        List<User> users = userRepository.findAllById(request.getUserIds());
        String token = tokenCacheConfig.getToken("zoomToken", tokenCacheConfig.tokenCache());

        MeetingRequest zoomRequest = MeetingRequest.builder()
                .topic(request.getTitle())
                .start_time(request.getDate())
                .duration(request.getDuration())
                .build();

        Map<String, Object> response = zoomAPI.createMeeting("joaodantas94@gmail.com", zoomRequest, "Bearer " + token); //todo remove hardcoded values

        Meeting meeting = Meeting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .startTime(request.getDate())
                .endTime(request.getDate().plusMinutes(request.getDuration()))
                .date(request.getDate())
                .zoomLink((String) response.get("join_url"))
                .zoomMeetingId((Long) response.get("id"))
                .zoomPassword((String) response.get("password"))
                .users(users)
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

        return meetingRepository.findAllByCondominiumId(1); //todo get user condominium id

    }

    @Override
    public void addUsersToMeeting(AddUsersToMeetingRequest request, Integer meetingId) {

        //todo check if user is admin of condominium
        //todo check if users are in condominium

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found"));

        meeting.getUsers().add(null); //todo input users list

        meetingRepository.save(meeting);

    }

}

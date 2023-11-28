package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.controller.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.request.CreateMeetingRequest;
import pt.iscte.condo.domain.Meeting;
import pt.iscte.condo.domain.User;
import pt.iscte.condo.proxy.ZoomAPI;
import pt.iscte.condo.repository.MeetingRepository;
import pt.iscte.condo.repository.UserRepository;
import pt.iscte.condo.service.ZoomService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoomServiceImpl implements ZoomService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final ZoomAPI zoomAPI;

    @Override
    public void createMeeting(CreateMeetingRequest request) {
        List<User> users = userRepository.findAllById(request.getUserIds());

        Map<String, Object> zoomRequest = new HashMap<>();
        zoomRequest.put("topic", request.getTitle());
        zoomRequest.put("start_time", request.getTitle());
        zoomRequest.put("duration", request.getTitle());

        Map<String, Object> response = zoomAPI.createMeeting("USER_ID", zoomRequest, token);

        Meeting meeting = Meeting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .startTime(request.getDate())
                .endTime(request.getDate().plusMinutes(request.getDuration()))
                .date(request.getDate())
                .zoomLink((String) response.get("join_url"))
                .zoomMeetingId((String) response.get("id"))
                .zoomPassword((String) response.get("password"))
                .users(users)
                .build();

        for (User user : users) {
            Map<String, Object> registrant = new HashMap<>();
            registrant.put("email", user.getEmail());
            registrant.put("first_name", user.getFirstName());
            registrant.put("last_name", user.getLastName());

            zoomAPI.addRegistrant(meeting.getZoomMeetingId(), registrant, "JWT_TOKEN");
        }

        meetingRepository.save(meeting);

    }

    @Override
    public List<Meeting> getAllMeetings() {

        return meetingRepository.findAllByCondominiumId(1); //todo get user condominium id

    }

    @Override
    public void addUsersToMeeting(AddUsersToMeetingRequest request) {

        //todo check if user is admin of condominium
        //todo check if users are in condominium

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found"));

        meeting.getUsers().add(null); //todo input users list

        meetingRepository.save(meeting);

    }


    @Cacheable(value = "zoomToken", unless = "#result == null") //todo how to make this with a private method?
    public String getAccessToken() {
        Map<String, Object> request = new HashMap<>();
        request.put("grant_type", "client_credentials");

        Map<String, Object> response = zoomAPI.getAccessToken("", "", request); //todo get client id and secret from properties

        return (String) response.get("access_token");
    }
}

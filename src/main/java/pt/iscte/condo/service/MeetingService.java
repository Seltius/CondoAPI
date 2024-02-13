package pt.iscte.condo.service;

import pt.iscte.condo.controller.dto.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.dto.request.CreateMeetingRequest;
import pt.iscte.condo.repository.entities.Meeting;

import java.util.List;

public interface MeetingService {

    void createMeeting(CreateMeetingRequest request);
    List<Meeting> getAllMeetings();
    void addUsersToMeeting(AddUsersToMeetingRequest request, Integer meetingId);

}

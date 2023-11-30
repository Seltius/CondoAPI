package pt.iscte.condo.service;

import pt.iscte.condo.controller.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.request.CreateMeetingRequest;
import pt.iscte.condo.domain.Meeting;

import java.util.List;

public interface ZoomService {

    void createMeeting(CreateMeetingRequest request);
    List<Meeting> getAllMeetings();
    void addUsersToMeeting(AddUsersToMeetingRequest request, Integer meetingId);

}

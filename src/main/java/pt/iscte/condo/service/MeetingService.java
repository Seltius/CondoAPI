package pt.iscte.condo.service;

import pt.iscte.condo.domain.Meeting;

import java.util.List;

public interface MeetingService {

    void createMeeting(Meeting meeting);
    List<Meeting> getAllMeetings();
    void addUsersToMeeting(Integer meetingId, Integer userId);

}

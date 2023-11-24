package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.domain.Meeting;
import pt.iscte.condo.repository.MeetingRepository;
import pt.iscte.condo.service.MeetingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;

    @Override
    public void createMeeting(Meeting meeting) {

        meetingRepository.save(meeting);

    }

    @Override
    public List<Meeting> getAllMeetings() {

        return meetingRepository.findAllByCondominiumId(1); //todo get user condominium id

    }

    @Override
    public void addUsersToMeeting(Integer meetingId, Integer userId) {

        //todo check if user is admin of condominium
        //todo check if users are in condominium

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found"));

        meeting.getUsers().add(null); //todo input users list

        meetingRepository.save(meeting);

    }
}

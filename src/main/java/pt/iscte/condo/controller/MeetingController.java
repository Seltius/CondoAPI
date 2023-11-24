package pt.iscte.condo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.domain.Meeting;
import pt.iscte.condo.service.MeetingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public void createMeeting(@RequestBody Meeting meeting) { //todo create input object
        // todo call zoom api to create meeting
        meetingService.createMeeting(meeting);
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() { //todo create output object
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PostMapping("/{meetingId}/users")
    public void addUsersToMeeting(@PathVariable Integer meetingId, @RequestBody Integer userId) {
        //todo handle response
        // todo create input object with a list of User ids
        meetingService.addUsersToMeeting(meetingId, userId);
    }

}

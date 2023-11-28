package pt.iscte.condo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.domain.Meeting;
import pt.iscte.condo.service.ZoomService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final ZoomService zoomService;

    @PostMapping
    public void createMeeting(@RequestBody Meeting meeting) { //todo create input object
        // todo call zoom api to create meeting
        zoomService.createMeeting(meeting);
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() { //todo create output object
        return ResponseEntity.ok(zoomService.getAllMeetings());
    }

    @PostMapping("/{meetingId}/users")
    public void addUsersToMeeting(@PathVariable Integer meetingId, @RequestBody Integer userId) {
        //todo handle response
        // todo create input object with a list of User ids
        zoomService.addUsersToMeeting(meetingId, userId);
    }

}

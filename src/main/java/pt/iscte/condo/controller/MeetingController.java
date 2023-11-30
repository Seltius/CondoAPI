package pt.iscte.condo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.controller.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.request.CreateMeetingRequest;
import pt.iscte.condo.domain.Meeting;
import pt.iscte.condo.service.ZoomService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final ZoomService zoomService;

    @PostMapping
    public void createMeeting(@RequestBody CreateMeetingRequest request) {
        zoomService.createMeeting(request);
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() { //todo create output object
        return ResponseEntity.ok(zoomService.getAllMeetings());
    }

    @PostMapping("/{meetingId}/users")
    public void addUsersToMeeting(@PathVariable Integer meetingId, @RequestBody AddUsersToMeetingRequest request) {
        zoomService.addUsersToMeeting(request, meetingId);
    }

}

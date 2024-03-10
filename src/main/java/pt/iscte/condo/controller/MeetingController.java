package pt.iscte.condo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.controller.dto.request.AddUsersToMeetingRequest;
import pt.iscte.condo.controller.dto.request.CreateMeetingRequest;
import pt.iscte.condo.repository.entities.Meeting;
import pt.iscte.condo.service.MeetingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public void createMeeting(@RequestBody @Valid CreateMeetingRequest request) throws Exception {
        meetingService.createMeeting(request);
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() { //todo create output object
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PostMapping("/{meetingId}/users")
    public void addUsersToMeeting(@PathVariable Integer meetingId, @RequestBody AddUsersToMeetingRequest request) {
        meetingService.addUsersToMeeting(request, meetingId);
    }

}

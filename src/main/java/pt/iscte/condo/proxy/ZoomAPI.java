package pt.iscte.condo.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.proxy.request.AuthTokenRequest;
import pt.iscte.condo.proxy.request.MeetingRequest;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "zoom", url = "https://api.zoom.us/")
public interface ZoomAPI {

    @PostMapping(value = "v2/users/{userId}/meetings", consumes = APPLICATION_JSON_VALUE)
    Map<String, Object> createMeeting(@PathVariable("userId") String userId, MeetingRequest request, @RequestHeader("Authorization") String token);

    @GetMapping("v2/users/{userId}/meetings")
    Map<String, Object> listMeetings(@PathVariable("userId") String userId, @RequestHeader("Authorization") String token);

    @PostMapping("v2/meetings/{meetingId}/registrants")
    void addRegistrant(@PathVariable("meetingId") Long meetingId, @RequestBody Map<String, Object> registrant, @RequestHeader("Authorization") String token);

    @GetMapping("v2/meetings/{meetingId}/recordings")
    Map<String, Object> getRecordings(@PathVariable("meetingId") String meetingId, @RequestHeader("Authorization") String token);

    @PostMapping(value = "oauth/token", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getAccessToken(@RequestHeader("Authorization") String base64Credentials, @RequestBody AuthTokenRequest request);

}

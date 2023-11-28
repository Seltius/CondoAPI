package pt.iscte.condo.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "zoom", url = "https://api.zoom.us/")
public interface ZoomAPI {

    @PostMapping("v2/users/{userId}/meetings")
    Map<String, Object> createMeeting(@PathVariable("userId") String userId, @RequestBody Map<String, Object> meeting, @RequestHeader("Authorization") String token);

    @GetMapping("v2/users/{userId}/meetings")
    Map<String, Object> listMeetings(@PathVariable("userId") String userId, @RequestHeader("Authorization") String token);

    @PostMapping("v2/meetings/{meetingId}/registrants")
    void addRegistrant(@PathVariable("meetingId") String meetingId, @RequestBody Map<String, Object> registrant, @RequestHeader("Authorization") String token);

    @GetMapping("v2/meetings/{meetingId}/recordings")
    Map<String, Object> getRecordings(@PathVariable("meetingId") String meetingId, @RequestHeader("Authorization") String token);

    @PostMapping("oauth/token")
    Map<String, Object> getAccessToken(@RequestHeader("CLIENT_ID") String clientId, @RequestHeader("CLIENT_SECRET") String clientSecret, @RequestBody Map<String, Object> registrant);

}

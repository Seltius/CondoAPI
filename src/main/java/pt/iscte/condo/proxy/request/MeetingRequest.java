package pt.iscte.condo.proxy.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRequest {

    private String topic;
    private LocalDateTime start_time;
    private Integer duration;
}

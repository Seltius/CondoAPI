package pt.iscte.condo.controller.dto.request;

import lombok.*;
import pt.iscte.condo.controller.dto.Topic;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMeetingRequest {

    private String title;
    private String description;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Integer> userIds;
    private Integer secretaryId;
    private List<Topic> topics;

}

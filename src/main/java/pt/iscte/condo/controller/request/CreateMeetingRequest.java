package pt.iscte.condo.controller.request;

import lombok.*;

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
    private String time;
    private Integer duration;
    private List<Integer> userIds;


}

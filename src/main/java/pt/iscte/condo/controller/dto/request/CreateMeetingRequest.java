package pt.iscte.condo.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.controller.dto.Attachment;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.controller.validation.ExtraFieldsCheck;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExtraFieldsCheck
public class CreateMeetingRequest {

    @NotBlank(message = "title is mandatory")
    private String title;

    @NotBlank(message = "description is mandatory")
    private String description;

    @NotNull(message = "date is mandatory")
    private LocalDateTime date;

    @NotNull(message = "startTime is mandatory")
    private LocalDateTime startTime;

    @NotNull(message = "endTime is mandatory")
    private LocalDateTime endTime;

    @NotEmpty(message = "userIds is mandatory")
    private List<Integer> userIds;

    private Integer secretaryId;

    @NotEmpty(message = "topics is mandatory")
    private List<Topic> topics;

    private List<Attachment> attachments;

    private LocalDateTime extraDate;
    private LocalDateTime extraStartTime;
    private LocalDateTime extraEndTime;

}

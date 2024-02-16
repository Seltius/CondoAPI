package pt.iscte.condo.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummarizeRequest {

    @NotBlank(message = "meetingId is mandatory")
    private Integer meetingId;

    @NotBlank(message = "transcriptName is mandatory")
    private String transcriptName;

    @NotBlank(message = "fileData is mandatory")
    private String fileData; //base64

}

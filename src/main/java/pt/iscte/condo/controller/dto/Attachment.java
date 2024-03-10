package pt.iscte.condo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    @NotBlank(message = "name is mandatory")
    String name;

    @NotBlank(message = "fileData is mandatory")
    private String fileData; //base64

}

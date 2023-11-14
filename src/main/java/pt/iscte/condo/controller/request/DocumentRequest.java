package pt.iscte.condo.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.enums.DocumentType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "type is mandatory")
    private DocumentType type;

    @NotBlank(message = "ownerId is mandatory")
    private Integer ownerId;

    @NotBlank(message = "fileData is mandatory")
    private String fileData; //base64

}

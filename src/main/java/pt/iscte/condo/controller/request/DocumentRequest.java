package pt.iscte.condo.controller.request;

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

    private String name;
    private DocumentType type;
    private Integer ownerId;
    private Integer uploaderId;
    private String fileData; // TODO Base64 encoded string (TO NOT FORGET)

}

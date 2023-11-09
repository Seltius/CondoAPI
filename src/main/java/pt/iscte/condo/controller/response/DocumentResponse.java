package pt.iscte.condo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.enums.DocumentType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {

    private Integer id;
    private String name;
    private DocumentType documentType;
    private LocalDateTime uploadDate;
    private String ownerName;
    private String uploaderName;
    private Byte[] fileData;

}

package pt.iscte.condo.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private Integer id;
    private String name;
    private String fileData; //byte[] in Base64

}

package pt.iscte.condo.proxy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateThreadResponse {

    private String id;
    private String object;
    private Long createdAt;
    private Map<String, Object> metadata;

}

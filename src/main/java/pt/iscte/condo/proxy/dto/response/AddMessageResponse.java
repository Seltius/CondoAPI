package pt.iscte.condo.proxy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.proxy.dto.Content;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMessageResponse {

    private String id;
    private String object;
    private Long createdAt;
    private String threadId;
    private String role;
    private List<Content> content;
    private List<String> fileIds;
    private String assistantId;
    private String runId;
    private Map<String, Object> metadata;

}

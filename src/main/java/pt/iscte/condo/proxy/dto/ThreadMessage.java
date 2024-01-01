package pt.iscte.condo.proxy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreadMessage {

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

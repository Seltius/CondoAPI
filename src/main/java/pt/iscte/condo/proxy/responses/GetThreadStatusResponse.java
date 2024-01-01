package pt.iscte.condo.proxy.responses;

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
public class GetThreadStatusResponse {

    private String id;
    private String object;
    private Long createdAt;
    private String assistantId;
    private String threadId;
    private String status;
    private Long startedAt;
    private Long expiresAt;
    private Long cancelledAt;
    private Long failedAt;
    private Long completedAt;
    private Long lastError;
    private String model;
    private String instructions;
    private List<Map<String, String>> tools;
    private List<String> fileIds;
    private Map<String, Object> metadata;

}
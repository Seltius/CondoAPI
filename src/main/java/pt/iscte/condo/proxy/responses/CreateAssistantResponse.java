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
public class CreateAssistantResponse {

        private String id;
        private String object;
        private Long createdAt;
        private String name;
        private String description;
        private String model;
        private String instructions;
        private List<Map<String, String>> tools;
        private List<String> fileIds;
        private Map<String, Object> metadata;

}

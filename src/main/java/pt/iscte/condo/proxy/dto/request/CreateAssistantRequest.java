package pt.iscte.condo.proxy.dto.request;

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
public class CreateAssistantRequest {

    private String instructions;
    private String name;
    private List<Map<String, String>> tools;
    private String model;

}

package pt.iscte.condo.proxy.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunThreadRequest {

    private String assistant_id;
    private String instructions;

}

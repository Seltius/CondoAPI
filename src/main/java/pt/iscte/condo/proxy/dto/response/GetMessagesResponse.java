package pt.iscte.condo.proxy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.proxy.dto.ThreadMessage;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMessagesResponse {

    private String object;
    private List<ThreadMessage> data;
    private String first_id; //todo firstId is the message that we want
    private String last_id;
    private String hasMore;

}

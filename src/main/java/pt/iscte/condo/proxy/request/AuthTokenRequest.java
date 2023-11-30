package pt.iscte.condo.proxy.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenRequest {

    private String grant_type;
    private String account_id;

}

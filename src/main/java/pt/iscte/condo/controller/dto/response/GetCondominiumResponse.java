package pt.iscte.condo.controller.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCondominiumResponse {

    private String id;
    private String name;
    private String address;
    private String parish;
    private String country;

}

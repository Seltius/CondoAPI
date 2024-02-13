package pt.iscte.condo.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCondominiumRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "address is mandatory")
    private String address;

    @NotBlank(message = "parish is mandatory")
    private String parish;

    @NotBlank(message = "county is mandatory")
    private String county;

}

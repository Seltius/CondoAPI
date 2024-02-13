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
public class InviteCondominiumMemberRequest {

    @NotBlank(message = "email is mandatory")
    private String email;

}

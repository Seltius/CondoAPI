package pt.iscte.condo.service;

import pt.iscte.condo.controller.dto.request.AuthenticationRequest;
import pt.iscte.condo.controller.dto.request.RegisterRequest;
import pt.iscte.condo.controller.dto.response.AuthenticationResponse;
import pt.iscte.condo.controller.dto.response.ValidateTokenResponse;

public interface AuthService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    ValidateTokenResponse validate(String bearer);


}

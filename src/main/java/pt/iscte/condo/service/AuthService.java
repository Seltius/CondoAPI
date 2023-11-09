package pt.iscte.condo.service;

import pt.iscte.condo.controller.request.AuthenticationRequest;
import pt.iscte.condo.controller.request.RegisterRequest;
import pt.iscte.condo.controller.response.AuthenticationResponse;
import pt.iscte.condo.controller.response.ValidateTokenResponse;

public interface AuthService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    ValidateTokenResponse validate(String bearer);


}

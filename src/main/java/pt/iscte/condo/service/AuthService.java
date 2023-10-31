package pt.iscte.condo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.iscte.condo.controller.request.AuthenticationRequest;
import pt.iscte.condo.controller.request.RegisterRequest;
import pt.iscte.condo.controller.response.AuthenticationResponse;
import pt.iscte.condo.controller.response.ValidateTokenResponse;
import pt.iscte.condo.model.Role;
import pt.iscte.condo.model.User;
import pt.iscte.condo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return getAuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return getAuthenticationResponse(jwtService.generateToken(user));
    }

    public ValidateTokenResponse validate(String token) {
        String email = jwtService.getUsername(token);

        if (email == null || email.isEmpty()) {
            return ValidateTokenResponse.builder()
                    .isValid(false)
                    .build();
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ValidateTokenResponse
                .builder()
                .isValid(jwtService.isTokenValid(token, user))
                .build();
    }

    private AuthenticationResponse getAuthenticationResponse(String token) {
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }

}

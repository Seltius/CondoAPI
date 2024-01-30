package pt.iscte.condo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.controller.request.AuthenticationRequest;
import pt.iscte.condo.controller.request.RegisterRequest;
import pt.iscte.condo.controller.response.AuthenticationResponse;
import pt.iscte.condo.controller.response.ValidateTokenResponse;
import pt.iscte.condo.service.AuthService;
import pt.iscte.condo.service.TranscriptService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final TranscriptService transcriptService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validate(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.validate(token));
    }

    @GetMapping("/test")
    public void test() {
        transcriptService.processTranscripts();
    }

}

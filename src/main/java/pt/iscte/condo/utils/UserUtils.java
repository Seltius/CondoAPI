package pt.iscte.condo.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.domain.User;
import pt.iscte.condo.repository.UserRepository;
import pt.iscte.condo.service.impl.JwtServiceImpl;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;
    private final HttpServletRequest request;

    public User getUserByBearer() {
        return userRepository.findByEmail(jwtService.getUsername(getBearer()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private String getBearer() {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

    }

}

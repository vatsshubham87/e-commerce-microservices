package org.microservicesrevision.auth.controller;

import org.microservicesrevision.auth.dto.UserDetailsDTO;
import org.microservicesrevision.auth.serviceImpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userServiceDetails;

    @Value("${gateway.secret}")
    private String gatewaySecret;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userServiceDetails) {
        this.authenticationManager = authenticationManager;
        this.userServiceDetails = userServiceDetails;
    }

    @PostMapping("/validate")
    public Mono<UserDetailsDTO> validateCredentials(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestHeader("X-Gateway-Secret") String secret) {

        // 1. Validate request from Gateway
        if (!gatewaySecret.equals(secret)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Gateway Secret"));
        }

        // 2. Validate Basic Auth header
        if (authorization == null || !authorization.startsWith("Basic ")) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing or invalid Authorization header"));
        }

        // 3. Decode Basic Auth credentials
        String base64Credentials = authorization.substring("Basic ".length()).trim();
        String decoded = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        String[] parts = decoded.split(":", 2);
        if (parts.length != 2) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed Basic Auth header"));
        }
        String username = parts[0];
        String password = parts[1];

        // 4. Authenticate using Spring Security’s AuthenticationManager
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            if (authentication.isAuthenticated()) {
                // 5. Extract user details (after successful authentication)
                UserDetails userDetails = userServiceDetails.loadUserByUsername(username);
                UserDetailsDTO dto = new UserDetailsDTO();
                dto.setUsername(userDetails.getUsername());
                dto.setPassword(userDetails.getPassword()); // Keep plain only for Gateway verification (no encode yet)
                dto.setAuthorities(userDetails.getAuthorities().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList()));
                return Mono.just(dto);
            } else {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
            }

        } catch (Exception ex) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        }
    }
}

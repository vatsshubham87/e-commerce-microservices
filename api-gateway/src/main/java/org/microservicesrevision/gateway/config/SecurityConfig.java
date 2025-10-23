package org.microservicesrevision.gateway.config;

import org.microservicesrevision.gateway.dto.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final WebClient.Builder webClientBuilder;

    @Value("${gateway.secret}")
    private String gatewaySecret;

    public SecurityConfig(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveAuthenticationManager authenticationManager) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/actuator/**", "/eureka/**", "/*/actuator/**").permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(authenticationManager)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        return http.build();
    }


    @Bean
    public ReactiveAuthenticationManager authenticationManager(WebClient.Builder webClientBuilder,
                                                               @Value("${gateway.secret}") String gatewaySecret) {
        return authentication -> {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            // Build Basic Auth header
            String basicAuth = "Basic " + Base64.getEncoder()
                    .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

            return webClientBuilder.build()
                    .post()
                    .uri("lb://auth-service/validate")
                    .header(HttpHeaders.AUTHORIZATION, basicAuth)
                    .header("X-Gateway-Secret", gatewaySecret)
                    .retrieve()
                    .bodyToMono(UserDetailsDTO.class)
                    .map(userDetails -> {
                        return new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(),
                                userDetails.getPassword(),
                                userDetails.getAuthorities().stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .toList()
                        );
                    });
        };
    }

}

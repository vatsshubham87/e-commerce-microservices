package org.microservicesrevision.auth.dto;

import lombok.*;

import java.util.List;


@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private String username;
    private String password;
    private List<String> authorities;
}

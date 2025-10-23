package org.microservicesrevision.gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDetailsDTO {
    private String username;
    private String password;
    private List<String> authorities;

}

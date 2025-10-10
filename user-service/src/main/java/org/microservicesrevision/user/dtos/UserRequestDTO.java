package org.microservicesrevision.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String country;
    private String state;
    private List<Long> roleIds;   // List of existing role IDs to assign
    private String createdBy;
    private String updatedBy;
}


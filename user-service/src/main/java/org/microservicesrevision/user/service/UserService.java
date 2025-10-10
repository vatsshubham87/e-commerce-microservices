package org.microservicesrevision.user.service;

import org.microservicesrevision.user.dtos.UserRequestDTO;
import org.microservicesrevision.user.dtos.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

}

package org.microservicesrevision.user.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.microservicesrevision.user.dtos.RoleDTO;
import org.microservicesrevision.user.dtos.UserRequestDTO;
import org.microservicesrevision.user.dtos.UserResponseDTO;
import org.microservicesrevision.user.model.Role;
import org.microservicesrevision.user.model.User;
import org.microservicesrevision.user.model.UserRole;
import org.microservicesrevision.user.repo.RoleRepository;
import org.microservicesrevision.user.repo.UserRepository;
import org.microservicesrevision.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        UserResponseDTO responseDTO;

        try{
            List<Role> roles = roleRepository.findAllById(requestDTO.getRoleIds());

            if (roles.size() != requestDTO.getRoleIds().size()) {
                throw new IllegalArgumentException("One or more role IDs are invalid.");
            }

            User user = User.builder()
                    .name(requestDTO.getName())
                    .email(requestDTO.getEmail())
                    .password(requestDTO.getPassword())
                    .phoneNumber(requestDTO.getPhoneNumber())
                    .country(requestDTO.getCountry())
                    .state(requestDTO.getState())
                    .createdBy(requestDTO.getCreatedBy())
                    .updatedBy(requestDTO.getUpdatedBy())
                    .build();

            List<UserRole> userRoles = roles.stream().map(role -> createUserRole(role, user)).toList();

            user.setRoles(userRoles);

            User savedUser = userRepository.save(user);

            List<RoleDTO> roleDTOS = savedUser.getRoles().stream().map(this::convertToRoleDTO).toList();

           responseDTO = UserResponseDTO.builder()
                    .name(savedUser.getName())
                    .email(savedUser.getEmail())
                    .state(savedUser.getState())
                   .country(savedUser.getCountry())
                    .phoneNumber(savedUser.getPhoneNumber())
                    .createdAt(savedUser.getCreatedAt())
                    .updatedAt(savedUser.getUpdatedAt())
                    .createdBy(savedUser.getCreatedBy())
                    .updatedBy(savedUser.getCreatedBy())
                    .roles(roleDTOS)
                    .build();

        }catch (Exception e){
            log.error("Error in creating user : ", e);
            throw new RuntimeException("User creation failed.", e);
        }

        return responseDTO;
    }

    private RoleDTO convertToRoleDTO(UserRole userRole) {

        Role role = userRole.getRole();

        return RoleDTO.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    private UserRole createUserRole(Role role, User user) {

        return UserRole.builder()
                .user(user)
                .role(role)
                .assignedAt(LocalDateTime.now())
                .grantedBy("1")
                .status("Active")
                .build();
    }
}

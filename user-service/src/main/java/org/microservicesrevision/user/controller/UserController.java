package org.microservicesrevision.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.microservicesrevision.user.dtos.UserRequestDTO;
import org.microservicesrevision.user.dtos.UserResponseDTO;
import org.microservicesrevision.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User APIs", description = "user create, update and delete")
public class UserController {

    @Value("${auth.secret}")
    private String authSecret;

    private final UserService userService;

    @PostMapping("/create-user")
    @Operation(summary = "create user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO){

        UserResponseDTO responseDTO = userService.createUser(requestDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}

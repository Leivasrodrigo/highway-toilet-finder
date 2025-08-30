package com.highwaytoiletfinder.auth.controller;

import com.highwaytoiletfinder.auth.commandStrategy.AuthCommandStrategies;
import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthCommandStrategies authCommandStrategies;

    @PostMapping("/user")
    public ResponseEntity<AuthResponseDTO> handleAuthCommand(@RequestBody AuthRequestDTO commandDTO,
                                                             @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if ("googleLogin".equalsIgnoreCase(commandDTO.getCommand())) {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing or invalid Authorization header");
            }
            String idToken = authHeader.substring(7);
            commandDTO.setIdToken(idToken);
        }

        AuthResponseDTO result = authCommandStrategies.execute(commandDTO.getCommand(), commandDTO);

        if ("register".equalsIgnoreCase(commandDTO.getCommand()) && result.getId() != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/users/{id}")
                    .buildAndExpand(result.getId())
                    .toUri();

            return ResponseEntity.created(location).body(result);
        } else {
            return ResponseEntity.ok(result);
        }    }
}

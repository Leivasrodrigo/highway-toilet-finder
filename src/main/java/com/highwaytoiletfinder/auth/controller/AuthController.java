package com.highwaytoiletfinder.auth.controller;

import com.highwaytoiletfinder.auth.commandStrategy.AuthCommandStrategies;
import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthCommandStrategies authCommandStrategies;

    @PostMapping("/user")
    public ResponseEntity<AuthResponseDTO> handleAuthCommand(@RequestBody AuthRequestDTO commandDTO) {
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

package com.highwaytoiletfinder.user.controller;

import com.highwaytoiletfinder.user.commandStrategy.UserCommandStrategies;
import com.highwaytoiletfinder.user.dto.request.UserCommandDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import com.highwaytoiletfinder.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserCommandStrategies userCommandStrategies;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable UUID id) {
        UserResponseDTO response = userService.getById(id);
        return ResponseEntity.ok(response);
        }

    @PostMapping
    public ResponseEntity<UserResponseDTO> handlePlaceCommand(@RequestBody @Valid UserCommandDTO commandDTO) {
        UserResponseDTO result = userCommandStrategies.execute(commandDTO.getCommand(), commandDTO);

        if ("create".equalsIgnoreCase(commandDTO.getCommand())) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(result.getId())
                    .toUri();
            return ResponseEntity.created(location).body(result);
        } else if ("delete".equalsIgnoreCase(commandDTO.getCommand())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

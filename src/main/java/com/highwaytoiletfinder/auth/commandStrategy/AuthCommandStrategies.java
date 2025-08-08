package com.highwaytoiletfinder.auth.commandStrategy;

import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthCommandStrategies {
    private final List<AuthCommandStrategy> strategies;

    public AuthCommandStrategies(List<AuthCommandStrategy> strategies) {
        this.strategies = strategies;
    }

    public AuthResponseDTO execute(String command, AuthRequestDTO dto) {
        return strategies.stream()
                .filter(s -> s.supports(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid auth command: " + command))
                .execute(dto);
    }
}

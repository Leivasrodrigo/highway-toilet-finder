package com.highwaytoiletfinder.user.commandStrategy;

import com.highwaytoiletfinder.user.dto.request.UserCommandDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserCommandStrategies {

    private final List<UserCommandStrategy> strategies;

    public UserCommandStrategies(List<UserCommandStrategy> strategies) {
        this.strategies = strategies;
    }

    public UserResponseDTO execute(String command, UserCommandDTO dto) {
        return strategies.stream()
                .filter(s -> s.supports(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid command: " + command))
                .execute(dto);
    }
}

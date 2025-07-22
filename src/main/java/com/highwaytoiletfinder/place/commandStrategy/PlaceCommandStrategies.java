package com.highwaytoiletfinder.place.commandStrategy;

import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceCommandStrategies {

    private final List<PlaceCommandStrategy> strategies;

    public PlaceCommandStrategies(List<PlaceCommandStrategy> strategies) {
        this.strategies = strategies;
    }

    public PlaceResponseDTO execute(String command, PlaceCommandDTO dto) {
        return strategies.stream()
                .filter(s -> s.supports(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid command: " + command))
                .execute(dto);
    }
}

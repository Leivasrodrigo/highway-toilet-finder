package com.highwaytoiletfinder.toilet.commandStrategy;

import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToiletCommandStrategies {

    private final List<ToiletCommandStrategy> strategies;

    public ToiletCommandStrategies(List<ToiletCommandStrategy> strategies) {
        this.strategies = strategies;
    }

    public ToiletResponseDTO execute(String command, ToiletCommandDTO dto) {
        return strategies.stream()
                .filter(s -> s.supports(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid command: " + command))
                .execute(dto);
    }
}

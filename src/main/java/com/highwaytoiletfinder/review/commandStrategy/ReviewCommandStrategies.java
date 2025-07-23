package com.highwaytoiletfinder.review.commandStrategy;

import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewCommandStrategies {

    private final List<ReviewCommandStrategy> strategies;

    public ReviewCommandStrategies(List<ReviewCommandStrategy> strategies) {
        this.strategies = strategies;
    }

    public ReviewResponseDTO execute(String command, ReviewCommandDTO dto) {
        return strategies.stream()
                .filter(s -> s.supports(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid command: " + command))
                .execute(dto);
    }
}

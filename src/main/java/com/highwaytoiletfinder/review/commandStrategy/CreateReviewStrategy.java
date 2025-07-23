package com.highwaytoiletfinder.review.commandStrategy;

import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateReviewStrategy implements ReviewCommandStrategy{

    private final ReviewService reviewService;

    @Override
    public boolean supports(String command) {
        return "create".equalsIgnoreCase(command);
    }

    @Override
    public ReviewResponseDTO execute(ReviewCommandDTO dto) {
        return reviewService.createReview(dto);
    }
}

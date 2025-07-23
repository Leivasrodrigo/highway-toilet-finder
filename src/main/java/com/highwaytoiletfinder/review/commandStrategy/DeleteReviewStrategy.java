package com.highwaytoiletfinder.review.commandStrategy;

import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteReviewStrategy implements ReviewCommandStrategy {

    private final ReviewService reviewService;

    @Override
    public boolean supports(String command) {
        return "delete".equalsIgnoreCase(command);
    }

    @Override
    public ReviewResponseDTO execute(ReviewCommandDTO dto) {
        return reviewService.deleteReview(dto.getId());
    }
}

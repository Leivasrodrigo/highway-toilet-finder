package com.highwaytoiletfinder.review.commandStrategy;

import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;

public interface ReviewCommandStrategy {
    boolean supports(String command);
    ReviewResponseDTO execute(ReviewCommandDTO dto);
}

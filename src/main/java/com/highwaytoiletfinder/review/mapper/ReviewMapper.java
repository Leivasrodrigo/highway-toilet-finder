package com.highwaytoiletfinder.review.mapper;

import com.highwaytoiletfinder.review.dto.request.ReviewRequestDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDTO dto, Toilet toilet, User user) {
        return Review.builder()
                .toilet(toilet)
                .user(user)
                .ratingGeneral(dto.getRatingGeneral())
                .ratingCleanliness(dto.getRatingCleanliness())
                .ratingMaintenance(dto.getRatingMaintenance())
                .comment(dto.getComment())
                .build();
    }

    public ReviewResponseDTO toResponseDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .ratingGeneral(review.getRatingGeneral())
                .ratingCleanliness(review.getRatingCleanliness())
                .ratingMaintenance(review.getRatingMaintenance())
                .comment(review.getComment())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .toiletId(review.getToilet().getId())
                .build();
    }
}

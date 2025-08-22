package com.highwaytoiletfinder.review.mapper;

import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

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

    public void updateEntityFromCommandDTO(ReviewCommandDTO dto, Review review) {

        if (dto.getRatingCleanliness() != null) {
            review.setRatingCleanliness(dto.getRatingCleanliness());
        }
        if (dto.getRatingMaintenance() != null) {
            review.setRatingMaintenance(dto.getRatingMaintenance());
        }
        if (dto.getComment() != null) {
            String trimmed = dto.getComment().trim();
            review.setComment(trimmed.isEmpty() ? null : trimmed);
        }

        review.setRatingGeneral(calculateRatingGeneral(dto.getRatingCleanliness(), dto.getRatingMaintenance()));
    }

    public Review toEntityFromCommandDTO(ReviewCommandDTO dto, User user, Toilet toilet) {
        return Review.builder()
                .id(dto.getId())
                .ratingGeneral(calculateRatingGeneral(dto.getRatingCleanliness(), dto.getRatingMaintenance()))
                .ratingCleanliness(dto.getRatingCleanliness())
                .ratingMaintenance(dto.getRatingMaintenance())
                .comment(dto.getComment() != null && !dto.getComment().trim().isEmpty() ? dto.getComment().trim() : null)
                .user(user)
                .toilet(toilet)
                .build();
    }

    private Double calculateRatingGeneral(Integer ratingCleanliness, Integer ratingMaintenance) {
        return ((ratingCleanliness + ratingMaintenance) / 2.0);
    }
}

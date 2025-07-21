package com.highwaytoiletfinder.review.mapper;

import com.highwaytoiletfinder.review.dto.request.ReviewRequestDTO;
import com.highwaytoiletfinder.review.dto.request.ReviewUpdateRequestDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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

    public void updateEntityFromDTO(ReviewUpdateRequestDTO dto, Review review) {
        if (dto.getRatingGeneral() != null) {
            review.setRatingGeneral(dto.getRatingGeneral());
        }
        if (dto.getRatingCleanliness() != null) {
            review.setRatingCleanliness(dto.getRatingCleanliness());
        }
        if (dto.getRatingMaintenance() != null) {
            review.setRatingMaintenance(dto.getRatingMaintenance());
        }
        if (dto.getComment() != null) {
            review.setComment(dto.getComment());
        }
    }
}

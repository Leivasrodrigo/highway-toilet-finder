package com.highwaytoiletfinder.review.service;

import com.highwaytoiletfinder.review.dto.request.ReviewRequestDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.mapper.ReviewMapper;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.review.repository.ReviewRepository;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ToiletRepository toiletRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewResponseDTO> getByToiletId(UUID toiletId) {
        return reviewRepository.findByToiletId(toiletId)
                .stream()
                .map(reviewMapper::toResponseDTO)
                .toList();
    }

    public Optional<ReviewResponseDTO> getById(UUID id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::toResponseDTO);
    }

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        UUID toiletId = dto.getToiletId();
        UUID userId = dto.getUserId();

        Toilet toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new RuntimeException("Toilet not found with id: " + toiletId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Review review = reviewMapper.toEntity(dto, toilet, user);
        review.setCreatedAt(Instant.now());
        Review saved = reviewRepository.save(review);

        updateToiletAvgRating(toilet);

        return reviewMapper.toResponseDTO(saved);
    }

    private void updateToiletAvgRating(Toilet toilet) {
        List<Review> reviews = reviewRepository.findByToiletId(toilet.getId());

        double avg = reviews.stream()
                .mapToInt(Review::getRatingGeneral)
                .average()
                .orElse(0);

        toilet.setAvgRating(avg);
        toilet.setTotalReviews(reviews.size());

        toiletRepository.save(toilet);
    }
}

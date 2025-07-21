package com.highwaytoiletfinder.review.service;

import com.highwaytoiletfinder.common.exceptions.PlaceNotFoundException;
import com.highwaytoiletfinder.common.exceptions.ReviewNotFoundException;
import com.highwaytoiletfinder.common.exceptions.ToiletNotFoundException;
import com.highwaytoiletfinder.common.exceptions.UserNotFoundException;
import com.highwaytoiletfinder.review.dto.request.ReviewRequestDTO;
import com.highwaytoiletfinder.review.dto.request.ReviewUpdateRequestDTO;
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

    public ReviewResponseDTO getById(UUID id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::toResponseDTO)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
    }

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        UUID toiletId = dto.getToiletId();
        UUID userId = dto.getUserId();

        Toilet toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + toiletId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Review review = reviewMapper.toEntity(dto, toilet, user);
        review.setCreatedAt(Instant.now());
        Review saved = reviewRepository.save(review);

        updateToiletAvgRating(toilet);

        return reviewMapper.toResponseDTO(saved);
    }

    public ReviewResponseDTO update(UUID id, ReviewUpdateRequestDTO dto) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));

        reviewMapper.updateEntityFromDTO(dto, existing);

        Review updated = reviewRepository.save(existing);
        return reviewMapper.toResponseDTO(updated);
    }

    public void delete(UUID id) {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review not found with id: " + id);
        }

        reviewRepository.deleteById(id);
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

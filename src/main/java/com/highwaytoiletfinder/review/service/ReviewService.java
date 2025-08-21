package com.highwaytoiletfinder.review.service;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.common.exceptions.ReviewNotFoundException;
import com.highwaytoiletfinder.common.security.AuthenticatedUserProvider;
import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.review.mapper.ReviewMapper;
import com.highwaytoiletfinder.review.repository.ReviewRepository;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ToiletRepository toiletRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final ToiletService toiletService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

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

    public ReviewResponseDTO createReview(ReviewCommandDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided for creation");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email);
        Toilet toilet = toiletService.findById(dto.getToiletId());

        if (toilet.getStatus() == Status.REJECTED) {
            throw new IllegalStateException("Cannot submit a review for a rejected toilet.");
        }

        boolean alreadyExists = reviewRepository.existsByUserIdAndToiletId(user.getId(), toilet.getId());
        if (alreadyExists) {
            throw new IllegalStateException("User has already submitted a review for this toilet.");
        }

        Review review = reviewMapper.toEntityFromCommandDTO(dto, user, toilet);
        review.setCreatedAt(Instant.now());
        Review saved = reviewRepository.save(review);
        updateToiletAvgRating(toilet);

        return reviewMapper.toResponseDTO(saved);
    }

    public ReviewResponseDTO updateReview(ReviewCommandDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID must be provided for update");
        }

        Review existing = reviewRepository.findById(dto.getId())
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + dto.getId()));

        User user = authenticatedUserProvider.getAuthenticatedUser();

        Toilet toilet = existing.getToilet();
        if (toilet.getStatus() == Status.REJECTED) {
            throw new IllegalStateException("Cannot update a review for a rejected toilet.");
        }

        boolean isOwner = existing.getUser().getId().equals(user.getId());

        if (!isOwner) {
            throw new AccessDeniedException("User cannot update this review");
        }

        reviewMapper.updateEntityFromCommandDTO(dto, existing);

        Review updated = reviewRepository.save(existing);
        return reviewMapper.toResponseDTO(updated);
    }
    
    public ReviewResponseDTO deleteReview(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));

        User user = authenticatedUserProvider.getAuthenticatedUser();

        boolean isOwner = review.getUser().getId().equals(user.getId());
        boolean isModeratorOrAdmin = user.getUserRole() == Role.ADMIN
                || user.getUserRole() == Role.MODERATOR;

        if (!isOwner && !isModeratorOrAdmin) {
            throw new AccessDeniedException("User cannot delete this review");
        }

        reviewRepository.deleteById(id);
        return new ReviewResponseDTO();
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

        toiletService.updateToiletStatusBasedOnReviews(toilet);
    }
}

package com.highwaytoiletfinder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ToiletRepository toiletRepository;
    private final UserRepository userRepository;

    public List<Review> getByToiletId(UUID toiletId) {
        return reviewRepository.findByToiletId(toiletId);
    }

    public Review getById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    public Review save(Review review) {
        UUID toiletId = review.getToilet().getId();
        UUID userId = review.getUser().getId();

        Toilet toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new RuntimeException("Toilet not found with id: " + toiletId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        review.setToilet(toilet);
        review.setUser(user);
        review.setCreatedAt(Instant.now());

        Review savedReview = reviewRepository.save(review);
        updateToiletAvgRating(toiletId);
        return savedReview;
    }

    private void updateToiletAvgRating(UUID toiletId) {
        List<Review> reviews = reviewRepository.findByToiletId(toiletId);

        double avg = reviews.stream()
                .mapToInt(Review::getRatingGeneral)
                .average()
                .orElse(0);

        Toilet toilet = toiletRepository.findById(toiletId).orElseThrow();
        toilet.setAvgRating(avg);
        toilet.setTotalReviews(reviews.size());

        toiletRepository.save(toilet);
    }
}

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

    public List<Review> getByToiletId(UUID toiletId) {
        return reviewRepository.findByToiletId(toiletId);
    }

    public Review getById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    public Review save(Review review) {
        UUID toiletId = review.getToilet().getId();

        Toilet toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new RuntimeException("Toilet not found with id: " + toiletId));

        review.setToilet(toilet);
        review.setCreatedAt(Instant.now());

        Review savedReview = reviewRepository.save(review);
        updateToiledAvgRating(toiletId);
        return savedReview;
    }

    private void updateToiledAvgRating(UUID toiletId) {
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

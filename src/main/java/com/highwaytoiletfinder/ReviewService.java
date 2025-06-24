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

    public List<Review> getByToiletId(UUID toiletId) {
        return reviewRepository.findByToiletId(toiletId);
    }

    public Review save(Review review) {
        review.setCreatedAt(Instant.now());
        return reviewRepository.save(review);
    }

    public Review getById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }
}

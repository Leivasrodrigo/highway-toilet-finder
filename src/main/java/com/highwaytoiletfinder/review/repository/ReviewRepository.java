package com.highwaytoiletfinder.review.repository;

import com.highwaytoiletfinder.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByToiletId(UUID toiletId);
    boolean existsByUserIdAndToiletId(UUID userId, UUID toiletId);
}

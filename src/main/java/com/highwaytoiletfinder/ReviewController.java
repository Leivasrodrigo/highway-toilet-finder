package com.highwaytoiletfinder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/toilet/{toiletId}")
    public ResponseEntity<List<Review>> getByToilet(@PathVariable UUID toiletId) {
        return ResponseEntity.ok(reviewService.getByToiletId(toiletId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody @Valid Review review) {
        Review savedReview = reviewService.save(review);
        return ResponseEntity
                .status(201)
                .body(savedReview);    }
}

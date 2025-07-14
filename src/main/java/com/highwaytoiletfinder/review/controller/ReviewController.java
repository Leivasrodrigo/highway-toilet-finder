package com.highwaytoiletfinder.review.controller;

import com.highwaytoiletfinder.review.dto.request.ReviewRequestDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/toilet/{toiletId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByToilet(@PathVariable UUID toiletId) {
        return ResponseEntity.ok(reviewService.getByToiletId(toiletId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable UUID id) {
        return reviewService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody @Valid ReviewRequestDTO requestDTO) {
        ReviewResponseDTO savedReview = reviewService.save(requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReview.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedReview);
    }
}

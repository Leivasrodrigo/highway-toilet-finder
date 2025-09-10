package com.highwaytoiletfinder.review.controller;

import com.highwaytoiletfinder.review.commandStrategy.ReviewCommandStrategies;
import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final ReviewCommandStrategies reviewCommandStrategies;
    private final ReviewService reviewService;

    @GetMapping("/toilet/{toiletId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByToilet(@PathVariable UUID toiletId) {
        return ResponseEntity.ok(reviewService.getByToiletId(toiletId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(reviewService.getByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable UUID id) {
        ReviewResponseDTO response = reviewService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> handleReviewCommand(@RequestBody @Valid ReviewCommandDTO commandDTO) {
        ReviewResponseDTO result = reviewCommandStrategies.execute(commandDTO.getCommand(), commandDTO);

        if ("create".equalsIgnoreCase(commandDTO.getCommand())) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(result.getId())
                    .toUri();
            return ResponseEntity.created(location).body(result);
        } else if ("delete".equalsIgnoreCase(commandDTO.getCommand())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
}

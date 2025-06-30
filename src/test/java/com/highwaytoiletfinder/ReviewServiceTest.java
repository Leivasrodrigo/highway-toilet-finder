package com.highwaytoiletfinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ToiletRepository toiletRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getByToiletId_shouldReturnReviewsForGivenToiletId() {
        UUID toiletId = UUID.randomUUID();

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setRatingGeneral(4);
        review1.setCreatedAt(Instant.now());

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setRatingGeneral(5);
        review2.setCreatedAt(Instant.now());

        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(review1, review2));

        List<Review> result = reviewService.getByToiletId(toiletId);

        assertEquals(2, result.size());
        assertEquals(4, result.get(0).getRatingGeneral());
        assertEquals(5, result.get(1).getRatingGeneral());
        verify(reviewRepository).findByToiletId(toiletId);
    }
}

package com.highwaytoiletfinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void getById_shouldReturnExistentReview() {
        UUID reviewId = UUID.randomUUID();

        Review review = new Review();
        review.setId(reviewId);
        review.setRatingGeneral(4);
        review.setRatingCleanliness(3);
        review.setRatingMaintenance(5);
        review.setComment("Limpeza razoável, mas bem conservado.");
        review.setCreatedAt(Instant.now());

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Review result = reviewService.getById(reviewId);

        assertEquals(4, result.getRatingGeneral());
        assertEquals(3, result.getRatingCleanliness());
        assertEquals("Limpeza razoável, mas bem conservado.", result.getComment());
        verify(reviewRepository).findById(reviewId);
    }

    @Test
    void save_shouldReturnCreatedReview() {
        UUID toiletId = UUID.randomUUID();
        Toilet toilet = new Toilet();
        toilet.setId(toiletId);

        Review review = new Review();
        review.setId(UUID.randomUUID());
        review.setToilet(toilet);
        review.setRatingGeneral(4);
        review.setRatingCleanliness(3);
        review.setRatingMaintenance(5);
        review.setComment("Banheiro limpo e funcional.");
        review.setCreatedAt(Instant.now());

        Review reviewToPost = new Review();
        reviewToPost.setToilet(toilet);
        reviewToPost.setRatingGeneral(4);
        reviewToPost.setRatingCleanliness(3);
        reviewToPost.setRatingMaintenance(5);
        reviewToPost.setComment("Banheiro limpo e funcional.");

        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toilet));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.save(reviewToPost);

        assertEquals(4, result.getRatingGeneral());
        assertEquals(3, result.getRatingCleanliness());
        assertEquals("Banheiro limpo e funcional.", result.getComment());
        verify(reviewRepository).save(reviewToPost);
    }

    @Test
    void updateToiletAvgRating_shouldUpdateToiletRating() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        UUID toiletId = UUID.randomUUID();
        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setName("Posto A");

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setRatingGeneral(4);

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setRatingGeneral(5);

        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(review1, review2));
        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toilet));
        when(toiletRepository.save(any(Toilet.class))).thenReturn(toilet);

        Method method = ReviewService.class.getDeclaredMethod("updateToiletAvgRating", UUID.class);
        method.setAccessible(true);
        method.invoke(reviewService, toiletId);

        assertEquals(4.5, toilet.getAvgRating());
        assertEquals(2, toilet.getTotalReviews());
        verify(toiletRepository).save(toilet);
    }
}

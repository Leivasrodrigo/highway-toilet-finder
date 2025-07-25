package com.highwaytoiletfinder;

import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.mapper.ReviewMapper;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.review.repository.ReviewRepository;
import com.highwaytoiletfinder.review.service.ReviewService;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock private UserService userService;
    @Mock private ToiletService toiletService;
    @Mock private ReviewRepository reviewRepository;
    @Mock private ToiletRepository toiletRepository;
    @Mock private ReviewMapper reviewMapper;

    @InjectMocks private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByToiletId_shouldReturnReviewsForGivenToiletId() {
        UUID toiletId = UUID.randomUUID();

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("John Doe");

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setUser(user1);
        review1.setRatingGeneral(4);
        review1.setCreatedAt(Instant.now());

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Maria Silva");

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setUser(user2);
        review2.setRatingGeneral(5);
        review2.setCreatedAt(Instant.now());

        ReviewResponseDTO response1 = new ReviewResponseDTO();
        response1.setId(review1.getId());
        response1.setUserId(user1.getId());
        response1.setUserName(user1.getName());
        response1.setRatingGeneral(4);

        ReviewResponseDTO response2 = new ReviewResponseDTO();
        response2.setId(review2.getId());
        response2.setUserId(user2.getId());
        response2.setUserName(user2.getName());
        response2.setRatingGeneral(5);

        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(review1, review2));
        when(reviewMapper.toResponseDTO(review1)).thenReturn(response1);
        when(reviewMapper.toResponseDTO(review2)).thenReturn(response2);

        List<ReviewResponseDTO> result = reviewService.getByToiletId(toiletId);

        assertEquals(2, result.size());
        assertEquals(4, result.get(0).getRatingGeneral());
        assertEquals("John Doe", result.get(0).getUserName());
        assertEquals(5, result.get(1).getRatingGeneral());
        assertEquals("Maria Silva", result.get(1).getUserName());

        verify(reviewRepository).findByToiletId(toiletId);
        verify(reviewMapper).toResponseDTO(review1);
        verify(reviewMapper).toResponseDTO(review2);
    }

    @Test
    void getById_shouldReturnReviewWhenFound() {
        UUID reviewId = UUID.randomUUID();
        UUID toiletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        Review review = new Review();
        review.setId(reviewId);
        review.setToilet(toilet);
        review.setUser(user);
        review.setRatingGeneral(4);
        review.setRatingCleanliness(3);
        review.setRatingMaintenance(5);
        review.setComment("Good place");
        review.setCreatedAt(Instant.now());

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
        responseDTO.setToiletId(toiletId);
        responseDTO.setUserId(userId);
        responseDTO.setUserName("John Doe");
        responseDTO.setRatingGeneral(4);
        responseDTO.setRatingCleanliness(3);
        responseDTO.setRatingMaintenance(5);
        responseDTO.setComment("Good place");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewMapper.toResponseDTO(review)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.getById(reviewId);

        assertEquals(reviewId, result.getId());
        assertEquals(4, result.getRatingGeneral());
        assertEquals(3, result.getRatingCleanliness());
        assertEquals("Good place", result.getComment());

        verify(reviewRepository).findById(reviewId);
        verify(reviewMapper).toResponseDTO(review);
    }

    @Test
    void createReview_shouldSaveAndReturnReviewAndUpdateToiletRating() {
        UUID toiletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        ReviewCommandDTO dto = new ReviewCommandDTO();
        dto.setUserId(userId);
        dto.setToiletId(toiletId);
        dto.setRatingGeneral(4);
        dto.setRatingCleanliness(3);
        dto.setRatingMaintenance(5);
        dto.setComment("Nice place");

        Review reviewEntity = new Review();
        reviewEntity.setUser(user);
        reviewEntity.setToilet(toilet);
        reviewEntity.setRatingGeneral(4);
        reviewEntity.setRatingCleanliness(3);
        reviewEntity.setRatingMaintenance(5);
        reviewEntity.setComment("Nice place");

        Review savedReview = new Review();
        savedReview.setId(reviewId);
        savedReview.setUser(user);
        savedReview.setToilet(toilet);
        savedReview.setRatingGeneral(4);
        savedReview.setRatingCleanliness(3);
        savedReview.setRatingMaintenance(5);
        savedReview.setComment("Nice place");
        savedReview.setCreatedAt(Instant.now());

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
        responseDTO.setUserId(userId);
        responseDTO.setToiletId(toiletId);
        responseDTO.setUserName("John Doe");
        responseDTO.setRatingGeneral(4);
        responseDTO.setRatingCleanliness(3);
        responseDTO.setRatingMaintenance(5);
        responseDTO.setComment("Nice place");

        when(userService.findById(userId)).thenReturn(user);
        when(toiletService.findById(toiletId)).thenReturn(toilet);
        when(reviewMapper.toEntityFromCommandDTO(dto, user, toilet)).thenReturn(reviewEntity);
        when(reviewRepository.save(reviewEntity)).thenReturn(savedReview);
        when(reviewMapper.toResponseDTO(savedReview)).thenReturn(responseDTO);

        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(savedReview));
        when(toiletRepository.save(toilet)).thenReturn(toilet);

        ReviewResponseDTO result = reviewService.createReview(dto);

        assertNotNull(result);
        assertEquals(reviewId, result.getId());
        assertEquals(4, result.getRatingGeneral());
        assertEquals("Nice place", result.getComment());

        verify(userService).findById(userId);
        verify(toiletService).findById(toiletId);
        verify(reviewMapper).toEntityFromCommandDTO(dto, user, toilet);
        verify(reviewRepository).save(reviewEntity);
        verify(reviewMapper).toResponseDTO(savedReview);

        verify(reviewRepository).findByToiletId(toiletId);
        verify(toiletRepository).save(toilet);
    }

    @Test
    void updateReview_shouldUpdateAndReturnReview() {
        UUID reviewId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID toiletId = UUID.randomUUID();

        ReviewCommandDTO dto = new ReviewCommandDTO();
        dto.setId(reviewId);
        dto.setUserId(userId);
        dto.setToiletId(toiletId);
        dto.setRatingGeneral(5);
        dto.setRatingCleanliness(4);
        dto.setRatingMaintenance(3);
        dto.setComment("Updated comment");

        User user = new User();
        user.setId(userId);

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);

        Review existingReview = new Review();
        existingReview.setId(reviewId);
        existingReview.setUser(user);
        existingReview.setToilet(toilet);
        existingReview.setRatingGeneral(3);
        existingReview.setRatingCleanliness(2);
        existingReview.setRatingMaintenance(1);
        existingReview.setComment("Old comment");

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
        responseDTO.setUserId(userId);
        responseDTO.setToiletId(toiletId);
        responseDTO.setUserName("John Doe");
        responseDTO.setRatingGeneral(5);
        responseDTO.setRatingCleanliness(4);
        responseDTO.setRatingMaintenance(3);
        responseDTO.setComment("Updated comment");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        doAnswer(invocation -> {
            ReviewCommandDTO commandDTO = invocation.getArgument(0);
            Review review = invocation.getArgument(1);
            review.setRatingGeneral(commandDTO.getRatingGeneral());
            review.setRatingCleanliness(commandDTO.getRatingCleanliness());
            review.setRatingMaintenance(commandDTO.getRatingMaintenance());
            review.setComment(commandDTO.getComment());
            return null;
        }).when(reviewMapper).updateEntityFromCommandDTO(dto, existingReview);

        when(reviewRepository.save(existingReview)).thenReturn(existingReview);
        when(reviewMapper.toResponseDTO(existingReview)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.updateReview(dto);

        assertEquals(5, result.getRatingGeneral());
        assertEquals(4, result.getRatingCleanliness());
        assertEquals("Updated comment", result.getComment());

        verify(reviewRepository).findById(reviewId);
        verify(reviewMapper).updateEntityFromCommandDTO(dto, existingReview);
        verify(reviewRepository).save(existingReview);
        verify(reviewMapper).toResponseDTO(existingReview);
    }

    @Test
    void deleteReview_shouldDeleteReviewAndReturnEmptyResponse() {
        UUID reviewId = UUID.randomUUID();

        Review review = new Review();
        review.setId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).deleteById(reviewId);

        ReviewResponseDTO result = reviewService.deleteReview(reviewId);

        assertNotNull(result);
        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void updateToiletAvgRating_privateMethod_shouldUpdateAvgRatingAndTotalReviews() throws Exception {
        UUID toiletId = UUID.randomUUID();

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setToilet(toilet);
        review1.setRatingGeneral(4);

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setToilet(toilet);
        review2.setRatingGeneral(5);

        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(review1, review2));
        when(toiletRepository.save(toilet)).thenReturn(toilet);

        Method method = ReviewService.class.getDeclaredMethod("updateToiletAvgRating", Toilet.class);
        method.setAccessible(true);
        method.invoke(reviewService, toilet);

        assertEquals(4.5, toilet.getAvgRating());
        assertEquals(2, toilet.getTotalReviews());

        verify(reviewRepository).findByToiletId(toiletId);
        verify(toiletRepository).save(toilet);
    }
}
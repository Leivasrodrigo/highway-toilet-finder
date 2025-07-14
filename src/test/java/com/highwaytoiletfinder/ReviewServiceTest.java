package com.highwaytoiletfinder;

import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.review.dto.request.ReviewRequestDTO;
import com.highwaytoiletfinder.review.mapper.ReviewMapper;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.review.repository.ReviewRepository;
import com.highwaytoiletfinder.review.service.ReviewService;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
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
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ToiletRepository toiletRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByToiletId_shouldReturnReviewsForGivenToiletId() {
        UUID toiletId = UUID.randomUUID();
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        User user1 = new User();
        user1.setId(userId1);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setUser(user1);
        review1.setRatingGeneral(4);
        review1.setCreatedAt(Instant.now());

        User user2 = new User();
        user2.setId(userId2);
        user2.setName("Getulio Pereira");
        user2.setEmail("getulio.pereira@example.com");

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setUser(user2);
        review2.setRatingGeneral(5);
        review2.setCreatedAt(Instant.now());

        ReviewResponseDTO response1 = new ReviewResponseDTO();
        response1.setId(review1.getId());
        response1.setUserId(userId1);
        response1.setUserName("John Doe");
        response1.setRatingGeneral(4);

        ReviewResponseDTO response2 = new ReviewResponseDTO();
        response2.setId(review2.getId());
        response2.setUserId(userId2);
        response2.setUserName("Getulio Pereira");
        response2.setRatingGeneral(5);

        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(review1, review2));
        when(reviewMapper.toResponseDTO(review1)).thenReturn(response1);
        when(reviewMapper.toResponseDTO(review2)).thenReturn(response2);

        List<ReviewResponseDTO> result = reviewService.getByToiletId(toiletId);

        assertEquals(2, result.size());
        assertEquals(4, result.get(0).getRatingGeneral());
        assertEquals(5, result.get(1).getRatingGeneral());
        assertEquals("John Doe", result.get(0).getUserName());
        assertEquals("Getulio Pereira", result.get(1).getUserName());

        verify(reviewRepository).findByToiletId(toiletId);
        verify(reviewMapper).toResponseDTO(review1);
        verify(reviewMapper).toResponseDTO(review2);
    }

    @Test
    void getById_shouldReturnExistentReview() {
        UUID toiletId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        Review review = new Review();
        review.setId(reviewId);
        review.setUser(user);
        review.setToilet(toilet);
        review.setRatingGeneral(4);
        review.setRatingCleanliness(3);
        review.setRatingMaintenance(5);
        review.setComment("Limpeza razoável, mas bem conservado.");
        review.setCreatedAt(Instant.now());

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(review.getId());
        response.setToiletId(toiletId);
        response.setUserId(userId);
        response.setUserName("John Doe");
        response.setRatingGeneral(4);
        response.setRatingCleanliness(3);
        response.setRatingMaintenance(5);
        response.setComment("Limpeza razoável, mas bem conservado.");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewMapper.toResponseDTO(review)).thenReturn(response);

        Optional<ReviewResponseDTO> result = reviewService.getById(reviewId);

        assertEquals(4, result.get().getRatingGeneral());
        assertEquals(3, result.get().getRatingCleanliness());
        assertEquals("Limpeza razoável, mas bem conservado.", result.get().getComment());
        verify(reviewRepository).findById(reviewId);
        verify(reviewMapper).toResponseDTO(review);
    }

    @Test
    void save_shouldReturnCreatedReview() {
        UUID toiletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setAvgRating(null);
        toilet.setTotalReviews(null);

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("test@example.com");

        ReviewRequestDTO requestDTO = new ReviewRequestDTO();
        requestDTO.setToiletId(toiletId);
        requestDTO.setUserId(userId);
        requestDTO.setRatingGeneral(4);
        requestDTO.setRatingCleanliness(3);
        requestDTO.setRatingMaintenance(5);
        requestDTO.setComment("Banheiro limpo e funcional.");

        Review reviewToSave = new Review();
        reviewToSave.setToilet(toilet);
        reviewToSave.setUser(user);
        reviewToSave.setRatingGeneral(4);
        reviewToSave.setRatingCleanliness(3);
        reviewToSave.setRatingMaintenance(5);
        reviewToSave.setComment("Banheiro limpo e funcional.");

        Review savedReview = new Review();
        savedReview.setId(reviewId);
        savedReview.setToilet(toilet);
        savedReview.setUser(user);
        savedReview.setRatingGeneral(4);
        savedReview.setRatingCleanliness(3);
        savedReview.setRatingMaintenance(5);
        savedReview.setComment("Banheiro limpo e funcional.");
        savedReview.setCreatedAt(Instant.now());

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
        responseDTO.setToiletId(toiletId);
        responseDTO.setUserId(userId);
        responseDTO.setUserName("John Doe");
        responseDTO.setRatingGeneral(4);
        responseDTO.setRatingCleanliness(3);
        responseDTO.setRatingMaintenance(5);
        responseDTO.setComment("Banheiro limpo e funcional.");

        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toilet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reviewMapper.toEntity(requestDTO, toilet, user)).thenReturn(reviewToSave);
        when(reviewRepository.save(reviewToSave)).thenReturn(savedReview);
        when(reviewMapper.toResponseDTO(savedReview)).thenReturn(responseDTO);
        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(savedReview));

        ReviewResponseDTO result = reviewService.save(requestDTO);

        assertEquals(reviewId, result.getId());
        assertEquals(4, result.getRatingGeneral());
        assertEquals(3, result.getRatingCleanliness());
        assertEquals("Banheiro limpo e funcional.", result.getComment());
        assertEquals(userId, result.getUserId());
        assertEquals(toiletId, result.getToiletId());

        verify(toiletRepository).findById(toiletId);
        verify(userRepository).findById(userId);
        verify(reviewMapper).toEntity(requestDTO, toilet, user);
        verify(reviewRepository).save(reviewToSave);
        verify(reviewMapper).toResponseDTO(savedReview);
    }


    @Test
    void updateToiletAvgRating_shouldUpdateToiletRating() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        UUID placeId = UUID.randomUUID();
        Place place = new Place();
        place.setId(placeId);
        place.setName("Posto A");
        place.setAddress("Av. Central, 1000");
        place.setLatitude(-27.12345);
        place.setLongitude(-48.98765);
        place.setGooglePlaceId("some-google-place-id");

        UUID toiletId = UUID.randomUUID();
        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setPlace(place);

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setRatingGeneral(4);
        review1.setToilet(toilet);

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setRatingGeneral(5);
        review2.setToilet(toilet);

        when(reviewRepository.findByToiletId(toiletId)).thenReturn(List.of(review1, review2));
        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toilet));
        when(toiletRepository.save(any(Toilet.class))).thenReturn(toilet);

        Method method = ReviewService.class.getDeclaredMethod("updateToiletAvgRating", Toilet.class);
        method.setAccessible(true);
        method.invoke(reviewService, toilet);

        assertEquals(4.5, toilet.getAvgRating());
        assertEquals(2, toilet.getTotalReviews());
        verify(toiletRepository).save(toilet);
    }
}
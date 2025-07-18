package com.highwaytoiletfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highwaytoiletfinder.review.dto.request.ReviewRequestDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.review.controller.ReviewController;
import com.highwaytoiletfinder.review.service.ReviewService;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getByToilet_shouldReturnReviewsForGivenToiletId() throws Exception {
        UUID toiletId = UUID.randomUUID();
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        User user1 = new User();
        user1.setId(userId1);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setId(userId2);
        user2.setName("Getulio Pereira");
        user2.setEmail("getulio.pereira@example.com");

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setUser(user1);
        review1.setRatingGeneral(4);
        review1.setRatingCleanliness(3);
        review1.setRatingMaintenance(5);
        review1.setComment("Limpeza razoável, mas bem conservado.");
        review1.setCreatedAt(Instant.now());

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setUser(user2);
        review2.setRatingGeneral(5);
        review2.setRatingCleanliness(5);
        review2.setRatingMaintenance(5);
        review2.setComment("Excelente!");
        review2.setCreatedAt(Instant.now());

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setGender(Gender.MALE);
        toilet.setReviews(List.of(review1, review2));

        review1.setToilet(toilet);
        review2.setToilet(toilet);

        ReviewResponseDTO response1 = new ReviewResponseDTO();
        response1.setId(review1.getId());
        response1.setUserId(userId1);
        response1.setUserName("John Doe");
        response1.setRatingGeneral(4);
        response1.setComment("Limpeza razoável, mas bem conservado.");

        ReviewResponseDTO response2 = new ReviewResponseDTO();
        response2.setId(review2.getId());
        response2.setUserId(userId2);
        response2.setUserName("Getulio Pereira");
        response2.setRatingGeneral(5);
        response2.setComment("Excelente!");

        when(reviewService.getByToiletId(toiletId)).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/reviews/toilet/" + toiletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].ratingGeneral").value(4))
                .andExpect(jsonPath("$[0].comment").value("Limpeza razoável, mas bem conservado."))
                .andExpect(jsonPath("$[1].ratingGeneral").value(5))
                .andExpect(jsonPath("$[1].comment").value("Excelente!"));
    }

    @Test
    void getById_shouldReturnSpecificReview() throws Exception {
        UUID toiletId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setGender(Gender.MALE);

        Review review = new Review();
        review.setId(reviewId);
        review.setToilet(toilet);
        review.setUser(user);
        review.setRatingGeneral(4);
        review.setRatingCleanliness(3);
        review.setRatingMaintenance(5);
        review.setComment("Limpeza razoável, mas bem conservado.");
        review.setCreatedAt(Instant.now());

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(review.getId());
        response.setUserId(userId);
        response.setUserName("John Doe");
        response.setRatingGeneral(4);
        response.setComment("Limpeza razoável, mas bem conservado.");

        when(reviewService.getById(reviewId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/reviews/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingGeneral").value(4))
                .andExpect(jsonPath("$.comment").value("Limpeza razoável, mas bem conservado."))
                .andExpect(jsonPath("$.userName").value("John Doe"));
    }

    @Test
    void create_shouldReturnCreatedReview() throws Exception {
        UUID toiletId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setGender(Gender.MALE);

        Review review = new Review();
        review.setId(reviewId);
        review.setToilet(toilet);
        review.setUser(user);
        review.setRatingGeneral(4);
        review.setRatingCleanliness(3);
        review.setRatingMaintenance(5);
        review.setComment("Limpeza razoável, mas bem conservado.");
        review.setCreatedAt(Instant.now());

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(review.getId());
        responseDTO.setUserId(userId);
        responseDTO.setUserName("John Doe");
        responseDTO.setRatingGeneral(4);
        responseDTO.setRatingCleanliness(3);
        responseDTO.setRatingMaintenance(5);
        responseDTO.setComment("Limpeza razoável, mas bem conservado.");

        ReviewRequestDTO requestDTO = new ReviewRequestDTO();
        requestDTO.setUserId(userId);
        requestDTO.setToiletId(toiletId);
        requestDTO.setRatingGeneral(4);
        requestDTO.setRatingCleanliness(3);
        requestDTO.setRatingMaintenance(5);
        requestDTO.setComment("Limpeza razoável, mas bem conservado.");

        String json = objectMapper.writeValueAsString(requestDTO);

        when(reviewService.save(any(ReviewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ratingGeneral").value(4))
                .andExpect(jsonPath("$.ratingCleanliness").value(3))
                .andExpect(jsonPath("$.ratingMaintenance").value(5))
                .andExpect(jsonPath("$.comment").value("Limpeza razoável, mas bem conservado."));
    }
}

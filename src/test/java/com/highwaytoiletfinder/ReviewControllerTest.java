package com.highwaytoiletfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setRatingGeneral(4);
        review1.setRatingCleanliness(3);
        review1.setRatingMaintenance(5);
        review1.setComment("Limpeza razoável, mas bem conservado.");
        review1.setCreatedAt(Instant.now());

        Review review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setRatingGeneral(5);
        review2.setRatingCleanliness(5);
        review2.setRatingMaintenance(5);
        review2.setComment("Excelente!");
        review2.setCreatedAt(Instant.now());

        when(reviewService.getByToiletId(toiletId)).thenReturn(List.of(review1, review2));

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
        UUID reviewId = UUID.randomUUID();

        Review review1 = new Review();
        review1.setId(reviewId);
        review1.setRatingGeneral(4);
        review1.setRatingCleanliness(3);
        review1.setRatingMaintenance(5);
        review1.setComment("Limpeza razoável, mas bem conservado.");
        review1.setCreatedAt(Instant.now());

        when(reviewService.getById(reviewId)).thenReturn(review1);

        mockMvc.perform(get("/api/reviews/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingGeneral").value(4))
                .andExpect(jsonPath("$.comment").value("Limpeza razoável, mas bem conservado."));
    }

    @Test
    void create_shouldReturnCreatedReview() throws Exception {
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

        when(reviewService.save(any(Review.class))).thenReturn(review);

        String reviewJson = objectMapper.writeValueAsString(reviewToPost);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ratingGeneral").value(4))
                .andExpect(jsonPath("$.comment").value("Banheiro limpo e funcional."));
    }
}

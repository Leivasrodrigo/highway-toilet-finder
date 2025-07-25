package com.highwaytoiletfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highwaytoiletfinder.review.commandStrategy.ReviewCommandStrategies;
import com.highwaytoiletfinder.review.dto.request.ReviewCommandDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.controller.ReviewController;
import com.highwaytoiletfinder.review.service.ReviewService;
import com.highwaytoiletfinder.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewCommandStrategies reviewCommandStrategies;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
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

        ReviewResponseDTO response1 = new ReviewResponseDTO();
        response1.setId(UUID.randomUUID());
        response1.setUserId(userId1);
        response1.setUserName("John Doe");
        response1.setRatingGeneral(4);
        response1.setComment("Limpeza razoável, mas bem conservado.");

        ReviewResponseDTO response2 = new ReviewResponseDTO();
        response2.setId(UUID.randomUUID());
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
        UUID reviewId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(reviewId);
        response.setUserId(userId);
        response.setUserName("John Doe");
        response.setRatingGeneral(4);
        response.setComment("Limpeza razoável, mas bem conservado.");

        when(reviewService.getById(reviewId)).thenReturn(response);

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

        ReviewCommandDTO requestDTO = new ReviewCommandDTO();
        requestDTO.setCommand("create");
        requestDTO.setUserId(userId);
        requestDTO.setToiletId(toiletId);
        requestDTO.setRatingGeneral(4);
        requestDTO.setRatingCleanliness(3);
        requestDTO.setRatingMaintenance(5);
        requestDTO.setComment("Limpeza razoável, mas bem conservado.");

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
        responseDTO.setUserId(userId);
        responseDTO.setUserName("John Doe");
        responseDTO.setRatingGeneral(4);
        responseDTO.setRatingCleanliness(3);
        responseDTO.setRatingMaintenance(5);
        responseDTO.setComment("Limpeza razoável, mas bem conservado.");

        String json = objectMapper.writeValueAsString(requestDTO);

        when(reviewCommandStrategies.execute(eq("create"), any(ReviewCommandDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ratingGeneral").value(4))
                .andExpect(jsonPath("$.ratingCleanliness").value(3))
                .andExpect(jsonPath("$.ratingMaintenance").value(5))
                .andExpect(jsonPath("$.comment").value("Limpeza razoável, mas bem conservado."));
    }

    @Test
    void update_shouldReturnUpdatedReview() throws Exception {
        UUID reviewId = UUID.randomUUID();
        UUID toiletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ReviewCommandDTO requestDTO = new ReviewCommandDTO();
        requestDTO.setCommand("update");
        requestDTO.setId(reviewId);
        requestDTO.setUserId(userId);
        requestDTO.setToiletId(toiletId);
        requestDTO.setRatingGeneral(5);
        requestDTO.setRatingCleanliness(4);
        requestDTO.setRatingMaintenance(5);
        requestDTO.setComment("Atualizado.");

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
        responseDTO.setUserId(userId);
        responseDTO.setUserName("John Doe");
        responseDTO.setRatingGeneral(5);
        responseDTO.setRatingCleanliness(4);
        responseDTO.setRatingMaintenance(5);
        responseDTO.setComment("Atualizado.");

        String json = objectMapper.writeValueAsString(requestDTO);

        when(reviewCommandStrategies.execute(eq("update"), any(ReviewCommandDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingGeneral").value(5))
                .andExpect(jsonPath("$.ratingCleanliness").value(4))
                .andExpect(jsonPath("$.ratingMaintenance").value(5))
                .andExpect(jsonPath("$.comment").value("Atualizado."));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        UUID reviewId = UUID.randomUUID();

        ReviewCommandDTO requestDTO = new ReviewCommandDTO();
        requestDTO.setCommand("delete");
        requestDTO.setId(reviewId);

        String json = objectMapper.writeValueAsString(requestDTO);

        when(reviewCommandStrategies.execute(eq("delete"), any(ReviewCommandDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }
}

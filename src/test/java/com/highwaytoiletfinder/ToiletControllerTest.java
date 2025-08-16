package com.highwaytoiletfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.toilet.commandStrategy.ToiletCommandStrategies;
import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.controller.ToiletController;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ToiletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ToiletService toiletService;

    @Mock
    private ToiletCommandStrategies toiletCommandStrategies;

    @InjectMocks
    private ToiletController toiletController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(toiletController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAll_shouldReturnAllToilets() throws Exception {
        UUID toiletId1 = UUID.randomUUID();
        UUID toiletId2 = UUID.randomUUID();

        ToiletResponseDTO toiletDTO1 = new ToiletResponseDTO();
        toiletDTO1.setId(toiletId1);
        toiletDTO1.setGender(Gender.UNISEX);
        toiletDTO1.setHasAccessible(true);
        toiletDTO1.setHasBabyChanger(false);
        toiletDTO1.setHasShower(true);
        toiletDTO1.setAvgRating(4.5);
        toiletDTO1.setTotalReviews(10);
        toiletDTO1.setStatus(Status.APPROVED);
        toiletDTO1.setPlace(null);

        ToiletResponseDTO toiletDTO2 = new ToiletResponseDTO();
        toiletDTO2.setId(toiletId2);
        toiletDTO2.setGender(Gender.UNISEX);
        toiletDTO2.setHasAccessible(true);
        toiletDTO2.setHasBabyChanger(false);
        toiletDTO2.setHasShower(true);
        toiletDTO2.setAvgRating(4.5);
        toiletDTO2.setTotalReviews(10);
        toiletDTO2.setStatus(Status.APPROVED);
        toiletDTO2.setPlace(null);

        when(toiletService.getAll()).thenReturn(List.of(toiletDTO1, toiletDTO2));

        mockMvc.perform(get("/api/toilets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(toiletId1.toString()))
                .andExpect(jsonPath("$[1].id").value(toiletId2.toString()));
    }

    @Test
    void getById_shouldReturnToilet() throws Exception {
        UUID toiletId = UUID.randomUUID();

        ToiletResponseDTO toiletDTO = new ToiletResponseDTO();
        toiletDTO.setId(toiletId);
        toiletDTO.setGender(Gender.UNISEX);
        toiletDTO.setHasAccessible(true);
        toiletDTO.setHasBabyChanger(false);
        toiletDTO.setHasShower(true);
        toiletDTO.setAvgRating(4.5);
        toiletDTO.setTotalReviews(10);
        toiletDTO.setStatus(Status.APPROVED);
        toiletDTO.setPlace(null);

        when(toiletService.getById(toiletId)).thenReturn(toiletDTO);

        mockMvc.perform(get("/api/toilets/" + toiletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(toiletId.toString()))
                .andExpect(jsonPath("$.gender").value("UNISEX"))
                .andExpect(jsonPath("$.hasShower").value(true));
    }

    @Test
    void handleToiletCommand_shouldReturnCreatedToilet() throws Exception {
        UUID toiletId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();

        ToiletResponseDTO responseDTO = new ToiletResponseDTO();
        responseDTO.setId(toiletId);
        responseDTO.setGender(Gender.UNISEX);
        responseDTO.setHasAccessible(true);
        responseDTO.setHasBabyChanger(false);
        responseDTO.setHasShower(true);
        responseDTO.setAvgRating(null);
        responseDTO.setTotalReviews(null);
        responseDTO.setStatus(Status.PENDING);
        responseDTO.setPlace(null);

        ToiletCommandDTO requestDTO = new ToiletCommandDTO();
        requestDTO.setCommand("create");
        requestDTO.setPlaceId(placeId);
        requestDTO.setGender(Gender.UNISEX);
        requestDTO.setHasAccessible(true);
        requestDTO.setHasBabyChanger(false);
        requestDTO.setHasShower(true);

        String json = objectMapper.writeValueAsString(requestDTO);

        when(toiletCommandStrategies.execute(eq("create"), any(ToiletCommandDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/toilets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(toiletId.toString()))
                .andExpect(jsonPath("$.gender").value("UNISEX"))
                .andExpect(jsonPath("$.hasShower").value(true));
    }

    @Test
    void update_shouldReturnUpdatedToilet() throws Exception {
        UUID toiletId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();

        ToiletResponseDTO responseDTO = new ToiletResponseDTO();
        responseDTO.setId(toiletId);
        responseDTO.setGender(Gender.UNISEX);
        responseDTO.setHasAccessible(false);
        responseDTO.setHasBabyChanger(true);
        responseDTO.setHasShower(false);
        responseDTO.setAvgRating(4.0);
        responseDTO.setTotalReviews(5);
        responseDTO.setStatus(Status.APPROVED);
        responseDTO.setPlace(null);

        ToiletCommandDTO requestDTO = new ToiletCommandDTO();
        requestDTO.setCommand("update");
        requestDTO.setId(toiletId);
        requestDTO.setPlaceId(placeId);
        requestDTO.setGender(Gender.UNISEX);
        requestDTO.setHasAccessible(false);
        requestDTO.setHasBabyChanger(true);
        requestDTO.setHasShower(false);

        String json = objectMapper.writeValueAsString(requestDTO);

        when(toiletCommandStrategies.execute(eq("update"), any(ToiletCommandDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/toilets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(toiletId.toString()))
                .andExpect(jsonPath("$.hasBabyChanger").value(true))
                .andExpect(jsonPath("$.hasAccessible").value(false))
                .andExpect(jsonPath("$.hasShower").value(false));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        UUID toiletId = UUID.randomUUID();

        ToiletCommandDTO requestDTO = new ToiletCommandDTO();
        requestDTO.setCommand("delete");
        requestDTO.setId(toiletId);

        String json = objectMapper.writeValueAsString(requestDTO);

        when(toiletCommandStrategies.execute(eq("delete"), any(ToiletCommandDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/toilets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }
}

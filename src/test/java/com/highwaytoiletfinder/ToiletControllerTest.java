package com.highwaytoiletfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.toilet.dto.request.ToiletRequestDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.controller.ToiletController;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ToiletController.class)
public class ToiletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToiletService toiletService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAll_shouldReturnAllToilets() throws Exception {
        UUID placeId1 = UUID.randomUUID();
        UUID toiletId1 = UUID.randomUUID();

        PlaceResponseDTO placeDTO1 = new PlaceResponseDTO();
        placeDTO1.setId(placeId1);
        placeDTO1.setName("Posto A");
        placeDTO1.setAddress("Av. Central, 1000");
        placeDTO1.setLatitude(-27.12345);
        placeDTO1.setLongitude(-48.98765);
        placeDTO1.setGooglePlaceId("some-google-place-id");
        placeDTO1.setStatus(Status.PENDING);

        ToiletResponseDTO toiletDTO1 = new ToiletResponseDTO();
        toiletDTO1.setId(toiletId1);
        toiletDTO1.setPlace(placeDTO1);
        toiletDTO1.setGender(Gender.UNISEX);
        toiletDTO1.setHasAccessible(true);
        toiletDTO1.setHasBabyChanger(false);
        toiletDTO1.setHasShower(true);
        toiletDTO1.setAvgRating(4.5);
        toiletDTO1.setTotalReviews(10);
        toiletDTO1.setStatus(Status.APPROVED);

        UUID placeId2 = UUID.randomUUID();
        UUID toiletId2 = UUID.randomUUID();

        PlaceResponseDTO placeDTO2 = new PlaceResponseDTO();
        placeDTO2.setId(placeId2);
        placeDTO2.setName("Posto B");
        placeDTO2.setAddress("Av. Central, 1000");
        placeDTO2.setLatitude(-27.12345);
        placeDTO2.setLongitude(-48.98765);
        placeDTO2.setGooglePlaceId("some-google-place-id");
        placeDTO2.setStatus(Status.PENDING);

        ToiletResponseDTO toiletDTO2 = new ToiletResponseDTO();
        toiletDTO2.setId(toiletId2);
        toiletDTO2.setPlace(placeDTO2);
        toiletDTO2.setGender(Gender.UNISEX);
        toiletDTO2.setHasAccessible(true);
        toiletDTO2.setHasBabyChanger(false);
        toiletDTO2.setHasShower(true);
        toiletDTO2.setAvgRating(4.5);
        toiletDTO2.setTotalReviews(10);
        toiletDTO2.setStatus(Status.APPROVED);

        when(toiletService.getAll()).thenReturn(List.of(toiletDTO1, toiletDTO2));

        mockMvc.perform(get("/api/toilets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(toiletId1.toString()))
                .andExpect(jsonPath("$[1].id").value(toiletId2.toString()))
                .andExpect(jsonPath("$[0].place.name").value("Posto A"))
                .andExpect(jsonPath("$[1].place.name").value("Posto B"));
    }

    @Test
    void getById_shouldReturnToilet() throws Exception {
        UUID placeId = UUID.randomUUID();
        UUID toiletId = UUID.randomUUID();

        PlaceResponseDTO placeDTO = new PlaceResponseDTO();
        placeDTO.setId(placeId);
        placeDTO.setName("Posto A");
        placeDTO.setAddress("Av. Central, 1000");
        placeDTO.setLatitude(-27.12345);
        placeDTO.setLongitude(-48.98765);
        placeDTO.setGooglePlaceId("some-google-place-id");
        placeDTO.setStatus(Status.PENDING);

        ToiletResponseDTO toiletDTO = new ToiletResponseDTO();
        toiletDTO.setId(toiletId);
        toiletDTO.setPlace(placeDTO);
        toiletDTO.setGender(Gender.UNISEX);
        toiletDTO.setHasAccessible(true);
        toiletDTO.setHasBabyChanger(false);
        toiletDTO.setHasShower(true);
        toiletDTO.setAvgRating(4.5);
        toiletDTO.setTotalReviews(10);
        toiletDTO.setStatus(Status.APPROVED);

        when(toiletService.getById(toiletId)).thenReturn(Optional.of(toiletDTO));

        mockMvc.perform(get("/api/toilets/" + toiletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(toiletId.toString()))
                .andExpect(jsonPath("$.place.name").value("Posto A"))
                .andExpect(jsonPath("$.place.latitude").value(-27.12345))
                .andExpect(jsonPath("$.place.longitude").value(-48.98765))
                .andExpect(jsonPath("$.place.address").value("Av. Central, 1000"));
    }

    @Test
    void create_shouldReturnCreatedToilet() throws Exception {
        UUID placeId = UUID.randomUUID();
        UUID toiletId = UUID.randomUUID();

        PlaceResponseDTO placeDTO = new PlaceResponseDTO();
        placeDTO.setId(placeId);
        placeDTO.setName("Posto A");
        placeDTO.setAddress("Av. Central, 1000");
        placeDTO.setLatitude(-27.12345);
        placeDTO.setLongitude(-48.98765);
        placeDTO.setGooglePlaceId("some-google-place-id");
        placeDTO.setStatus(Status.PENDING);

        ToiletResponseDTO responseDTO = new ToiletResponseDTO();
        responseDTO.setId(toiletId);
        responseDTO.setGender(Gender.UNISEX);
        responseDTO.setHasAccessible(false);
        responseDTO.setHasBabyChanger(true);
        responseDTO.setHasShower(true);
        responseDTO.setAvgRating(null);
        responseDTO.setTotalReviews(null);
        responseDTO.setStatus(Status.PENDING);
        responseDTO.setPlace(placeDTO);

        ToiletRequestDTO requestDTO = new ToiletRequestDTO();
        requestDTO.setPlaceId(placeId);
        requestDTO.setGender(Gender.UNISEX);
        requestDTO.setHasAccessible(true);
        requestDTO.setHasBabyChanger(false);
        requestDTO.setHasShower(true);

        String json = objectMapper.writeValueAsString(requestDTO);

        when(toiletService.save(any(ToiletRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/toilets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.place.name").value("Posto A"))
                .andExpect(jsonPath("$.gender").value("UNISEX"))
                .andExpect(jsonPath("$.hasShower").value(true))
                .andExpect(jsonPath("$.place.latitude").value(-27.12345));
    }
}

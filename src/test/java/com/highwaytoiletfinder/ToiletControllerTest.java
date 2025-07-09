package com.highwaytoiletfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        Place place1 = new Place();
        place1.setId(placeId1);
        place1.setName("Posto A");
        place1.setAddress("Av. Central, 1000");
        place1.setLatitude(-27.12345);
        place1.setLongitude(-48.98765);
        place1.setGooglePlaceId("some-google-place-id");

        UUID toiletId1 = UUID.randomUUID();
        Toilet toilet1 = new Toilet();
        toilet1.setId(toiletId1);
        toilet1.setPlace(place1);

        UUID placeId2 = UUID.randomUUID();
        Place place2 = new Place();
        place2.setId(placeId2);
        place2.setName("Posto B");
        place2.setAddress("Av. Central, 1000");
        place2.setLatitude(-27.12345);
        place2.setLongitude(-48.98765);
        place2.setGooglePlaceId("some-google-place-id");

        UUID toiletId2 = UUID.randomUUID();
        Toilet toilet2 = new Toilet();
        toilet2.setId(toiletId2);
        toilet2.setPlace(place2);

        when(toiletService.getAll()).thenReturn(List.of(toilet1, toilet2));

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
        Place place = new Place();
        place.setId(placeId);
        place.setName("Posto A");
        place.setAddress("Av. Central, 1000");
        place.setLatitude(-27.12345);
        place.setLongitude(-48.98765);
        place.setGooglePlaceId("some-google-place-id");

        Toilet toilet = new Toilet();
        UUID toiletId = UUID.randomUUID();
        toilet.setId(toiletId);
        toilet.setPlace(place);

        when(toiletService.getById(toiletId)).thenReturn(Optional.of(toilet));

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

        String json = """
    {
        "hasMale": true,
        "hasFemale": true,
        "hasAccessible": false,
        "hasBabyChanger": true,
        "place": {
            "id": "%s"
        }
    }
    """.formatted(placeId);

        when(toiletService.save(any(Toilet.class))).thenReturn(toilet);

        mockMvc.perform(post("/api/toilets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.place.name").value("Posto A"));
    }
}

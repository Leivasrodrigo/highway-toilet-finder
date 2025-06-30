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
        Toilet toilet1 = new Toilet();
        UUID firstId = UUID.randomUUID();
        toilet1.setId(firstId);
        toilet1.setName("Posto A");

        Toilet toilet2 = new Toilet();
        UUID secondId = UUID.randomUUID();
        toilet2.setId(secondId);
        toilet2.setName("Posto B");

        when(toiletService.getAll()).thenReturn(List.of(toilet1, toilet2));

        mockMvc.perform(get("/api/toilets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(firstId.toString()))
                .andExpect(jsonPath("$[1].id").value(secondId.toString()))
                .andExpect(jsonPath("$[0].name").value("Posto A"))
                .andExpect(jsonPath("$[1].name").value("Posto B"));
    }

    @Test
    void getById_shouldReturnToilet() throws Exception {
        Toilet toilet = new Toilet();
        UUID toiletId = UUID.randomUUID();
        toilet.setId(toiletId);
        toilet.setName("Posto A");

        when(toiletService.getById(toiletId)).thenReturn(Optional.of(toilet));

        mockMvc.perform(get("/api/toilets/" + toiletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(toiletId.toString()))
                .andExpect(jsonPath("$.name").value("Posto A"));
    }

    @Test
    void create_shouldReturnCreatedToilet() throws Exception {
        UUID toiletId = UUID.randomUUID();
        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setName("Posto A");

        Toilet toiletToPost = new Toilet();
        toiletToPost.setName("Posto A");

        when(toiletService.save(any(Toilet.class))).thenReturn(toilet);

        String toiletJson = objectMapper.writeValueAsString(toiletToPost);

        mockMvc.perform(post("/api/toilets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toiletJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Posto A"));
    }
}

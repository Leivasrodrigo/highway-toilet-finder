package com.highwaytoiletfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.placeImport.controller.PlaceImportController;
import com.highwaytoiletfinder.placeImport.service.PlaceImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlaceImportControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PlaceImportService placeImportService;

    @InjectMocks
    private PlaceImportController placeImportController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(placeImportController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void importPlaces_shouldCallServiceAndReturnOk() throws Exception {
        NearbySearchRequest request = new NearbySearchRequest();
        request.setLocation("-27.12345,-48.98765");
        request.setRadius(1000);
        request.setType("gas_station");
        request.setKeyword("posto");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/import/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(placeImportService).importNearbyPlaces(request);
    }
}

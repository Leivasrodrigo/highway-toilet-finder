package com.highwaytoiletfinder;

import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponse;
import com.highwaytoiletfinder.googleplaces.service.GooglePlacesService;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.placeImport.service.PlaceImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.highwaytoiletfinder.common.enums.Status;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaceImportServiceTest {
    @Mock
    private GooglePlacesService googlePlacesService;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceImportService placeImportService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void importNearbyPlaces_shouldSaveOnlyNewPlaces() {
        NearbySearchRequest request = new NearbySearchRequest();
        request.setLocation("-27.123, -48.987");
        request.setRadius(1000);
        request.setKeyword("posto");
        request.setType("gas_station");

        NearbySearchResponse.Result result1 = new NearbySearchResponse.Result();
        result1.setName("Posto A");
        result1.setVicinity("Av. Central, 1000");
        result1.setPlace_id("google-123");

        NearbySearchResponse.Result.Geometry geometry1 = new NearbySearchResponse.Result.Geometry();
        NearbySearchResponse.Result.Geometry.Location location1 = new NearbySearchResponse.Result.Geometry.Location();
        location1.setLat(-27.123);
        location1.setLng(-48.987);
        geometry1.setLocation(location1);
        result1.setGeometry(geometry1);

        NearbySearchResponse.Result result2 = new NearbySearchResponse.Result();
        result2.setName("Posto B");
        result2.setVicinity("Rua B, 200");
        result2.setPlace_id("google-456");

        NearbySearchResponse.Result.Geometry geometry2 = new NearbySearchResponse.Result.Geometry();
        NearbySearchResponse.Result.Geometry.Location location2 = new NearbySearchResponse.Result.Geometry.Location();
        location2.setLat(-28.123);
        location2.setLng(-49.123);
        geometry2.setLocation(location2);
        result2.setGeometry(geometry2);

        NearbySearchResponse response = new NearbySearchResponse();
        response.setResults(List.of(result1, result2));

        when(googlePlacesService.searchNearby(request)).thenReturn(response);
        when(placeRepository.existsByGooglePlaceId("google-123")).thenReturn(false);
        when(placeRepository.existsByGooglePlaceId("google-456")).thenReturn(true);

        Place savedPlace = new Place();
        savedPlace.setId(UUID.randomUUID());
        savedPlace.setName("Posto A");
        savedPlace.setGooglePlaceId("google-123");
        savedPlace.setLatitude(-27.123);
        savedPlace.setLongitude(-48.987);
        savedPlace.setAddress("Av. Central, 1000");
        savedPlace.setStatus(Status.PENDING);

        when(placeRepository.saveAll(any())).thenReturn(List.of(savedPlace));

        List<Place> result = placeImportService.importNearbyPlaces(request);

        assertEquals(1, result.size());
        assertEquals("google-123", result.get(0).getGooglePlaceId());
        assertEquals("Posto A", result.get(0).getName());
        assertEquals(Status.PENDING, result.get(0).getStatus());

        verify(placeRepository).existsByGooglePlaceId("google-123");
        verify(placeRepository).existsByGooglePlaceId("google-456");
        verify(placeRepository).saveAll(any());
    }
}

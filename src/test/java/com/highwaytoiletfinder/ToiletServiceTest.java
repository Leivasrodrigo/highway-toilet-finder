package com.highwaytoiletfinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ToiletServiceTest {

    @Mock
    PlaceRepository placeRepository;

    @Mock
    private ToiletRepository toiletRepository;

    @InjectMocks
    private ToiletService toiletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnAllToilets() {
        UUID toiletId1 = UUID.randomUUID();
        Toilet toilet1 = new Toilet();
        toilet1.setId(toiletId1);

        UUID toiletId2 = UUID.randomUUID();
        Toilet toilet2 = new Toilet();
        toilet2.setId(toiletId2);

        when(toiletRepository.findAll()).thenReturn(List.of(toilet1, toilet2));

        List<Toilet> result = toiletService.getAll();

        assertEquals(2, result.size());
        assertEquals(toiletId1, result.get(0).getId());
        assertEquals(toiletId2, result.get(1).getId());
        verify(toiletRepository).findAll();
    }

    @Test
    void getById_shouldReturnExistentToilet() {
        UUID toiletId = UUID.randomUUID();
        Toilet toilet = new Toilet();
        toilet.setId(toiletId);


        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toilet));

        Optional<Toilet> result = toiletService.getById(toiletId);

        assertTrue(result.isPresent(), "Expected Optional to be present");
        assertEquals(toiletId, result.get().getId());
        verify(toiletRepository).findById(toiletId);
    }

    @Test
    void save_shouldReturnCreatedToilet() {
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
        toilet.setPlace(place);
        toilet.setId(toiletId);

        Toilet toiletToPost = new Toilet();
        toiletToPost.setPlace(place);

        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(toiletRepository.save(any(Toilet.class))).thenReturn(toilet);

        Toilet result = toiletService.save(toiletToPost);

        assertEquals(toiletId, result.getId());
        verify(toiletRepository).save(any(Toilet.class));
    }
}

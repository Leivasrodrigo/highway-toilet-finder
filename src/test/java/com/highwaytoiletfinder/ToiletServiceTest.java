package com.highwaytoiletfinder;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.place.service.PlaceService;
import com.highwaytoiletfinder.toilet.commandStrategy.ToiletCommandStrategies;
import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.request.ToiletRequestDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.mapper.ToiletMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ToiletServiceTest {

    @Mock
    private PlaceService placeService;

    @Mock
    private ToiletRepository toiletRepository;

    @Mock
    private ToiletMapper toiletMapper;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private ToiletService toiletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnAllToilets() {
        UUID placeId1 = UUID.randomUUID();
        Place place1 = new Place();
        place1.setId(placeId1);
        place1.setName("Posto A");
        place1.setAddress("Av. Central, 1000");
        place1.setLatitude(-27.12345);
        place1.setLongitude(-48.98765);
        place1.setGooglePlaceId("google-123");

        UUID toiletId1 = UUID.randomUUID();
        Toilet toilet1 = new Toilet();
        toilet1.setId(toiletId1);
        toilet1.setGender(Gender.UNISEX);
        toilet1.setHasAccessible(true);
        toilet1.setHasShower(false);
        toilet1.setHasBabyChanger(false);
        toilet1.setAvgRating(4.5);
        toilet1.setTotalReviews(10);
        toilet1.setStatus(Status.APPROVED);
        toilet1.setPlace(place1);

        PlaceResponseDTO placeDTO1 = new PlaceResponseDTO();
        placeDTO1.setId(placeId1);
        placeDTO1.setName("Posto A");
        placeDTO1.setAddress("Av. Central, 1000");
        placeDTO1.setLatitude(-27.12345);
        placeDTO1.setLongitude(-48.98765);
        placeDTO1.setGooglePlaceId("google-123");
        placeDTO1.setStatus(Status.APPROVED);

        ToiletResponseDTO responseDTO1 = new ToiletResponseDTO();
        responseDTO1.setId(toiletId1);
        responseDTO1.setGender(Gender.UNISEX);
        responseDTO1.setHasAccessible(true);
        responseDTO1.setHasShower(false);
        responseDTO1.setHasBabyChanger(false);
        responseDTO1.setAvgRating(4.5);
        responseDTO1.setTotalReviews(10);
        responseDTO1.setStatus(Status.APPROVED);
        responseDTO1.setPlace(placeDTO1);

        UUID placeId2 = UUID.randomUUID();
        Place place2 = new Place();
        place2.setId(placeId2);
        place2.setName("Posto B");
        place2.setAddress("Rua B, 200");
        place2.setLatitude(-28.54321);
        place2.setLongitude(-49.12345);
        place2.setGooglePlaceId("google-456");

        UUID toiletId2 = UUID.randomUUID();
        Toilet toilet2 = new Toilet();
        toilet2.setId(toiletId2);
        toilet2.setGender(Gender.MALE);
        toilet2.setHasAccessible(false);
        toilet2.setHasShower(true);
        toilet2.setHasBabyChanger(true);
        toilet2.setAvgRating(3.8);
        toilet2.setTotalReviews(5);
        toilet2.setStatus(Status.PENDING);
        toilet2.setPlace(place2);

        PlaceResponseDTO placeDTO2 = new PlaceResponseDTO();
        placeDTO2.setId(placeId2);
        placeDTO2.setName("Posto B");
        placeDTO2.setAddress("Rua B, 200");
        placeDTO2.setLatitude(-28.54321);
        placeDTO2.setLongitude(-49.12345);
        placeDTO2.setGooglePlaceId("google-456");
        placeDTO2.setStatus(Status.PENDING);

        ToiletResponseDTO responseDTO2 = new ToiletResponseDTO();
        responseDTO2.setId(toiletId2);
        responseDTO2.setGender(Gender.MALE);
        responseDTO2.setHasAccessible(false);
        responseDTO2.setHasShower(true);
        responseDTO2.setHasBabyChanger(true);
        responseDTO2.setAvgRating(3.8);
        responseDTO2.setTotalReviews(5);
        responseDTO2.setStatus(Status.PENDING);
        responseDTO2.setPlace(placeDTO2);

        when(toiletRepository.findAll()).thenReturn(List.of(toilet1, toilet2));
        when(toiletMapper.toResponseDTO(toilet1)).thenReturn(responseDTO1);
        when(toiletMapper.toResponseDTO(toilet2)).thenReturn(responseDTO2);

        List<ToiletResponseDTO> result = toiletService.getAll();
        assertEquals(toiletId1, result.get(0).getId());
        assertEquals(toiletId2, result.get(1).getId());

        assertEquals(Gender.UNISEX, result.get(0).getGender());
        assertEquals(Gender.MALE, result.get(1).getGender());

        verify(toiletRepository).findAll();
        verify(toiletMapper).toResponseDTO(toilet1);
        verify(toiletMapper).toResponseDTO(toilet2);
    }

    @Test
    void getById_shouldReturnExistentToilet() {
        UUID placeId = UUID.randomUUID();
        Place place = new Place();
        place.setId(placeId);
        place.setName("Posto A");
        place.setAddress("Av. Central, 1000");
        place.setLatitude(-27.12345);
        place.setLongitude(-48.98765);
        place.setGooglePlaceId("google-123");

        UUID toiletId = UUID.randomUUID();
        Toilet toilet = new Toilet();
        toilet.setId(toiletId);
        toilet.setGender(Gender.UNISEX);
        toilet.setHasAccessible(true);
        toilet.setHasShower(false);
        toilet.setHasBabyChanger(false);
        toilet.setAvgRating(4.5);
        toilet.setTotalReviews(10);
        toilet.setStatus(Status.APPROVED);
        toilet.setPlace(place);

        PlaceResponseDTO placeDTO = new PlaceResponseDTO();
        placeDTO.setId(placeId);
        placeDTO.setName("Posto A");
        placeDTO.setAddress("Av. Central, 1000");
        placeDTO.setLatitude(-27.12345);
        placeDTO.setLongitude(-48.98765);
        placeDTO.setGooglePlaceId("google-123");
        placeDTO.setStatus(Status.APPROVED);

        ToiletResponseDTO responseDTO = new ToiletResponseDTO();
        responseDTO.setId(toiletId);
        responseDTO.setGender(Gender.UNISEX);
        responseDTO.setHasAccessible(true);
        responseDTO.setHasShower(false);
        responseDTO.setHasBabyChanger(false);
        responseDTO.setAvgRating(4.5);
        responseDTO.setTotalReviews(10);
        responseDTO.setStatus(Status.APPROVED);
        responseDTO.setPlace(placeDTO);

        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toilet));
        when(toiletMapper.toResponseDTO(toilet)).thenReturn(responseDTO);

        ToiletResponseDTO result = toiletService.getById(toiletId);

        assertEquals(toiletId, result.getId());
        verify(toiletRepository).findById(toiletId);
    }

    @Test
    void createToilet_shouldCreateAndReturnToilet() {
        UUID placeId = UUID.randomUUID();
        ToiletCommandDTO dto = new ToiletCommandDTO();
        dto.setPlaceId(placeId);
        dto.setGender(Gender.UNISEX);
        dto.setHasAccessible(true);
        dto.setHasBabyChanger(false);
        dto.setHasShower(true);

        Place place = new Place();
        place.setId(placeId);
        place.setName("Posto A");
        place.setAddress("Av. Central, 1000");
        place.setLatitude(-27.12345);
        place.setLongitude(-48.98765);
        place.setGooglePlaceId("some-google-place-id");
        place.setStatus(Status.APPROVED);

        Toilet toiletToSave = new Toilet();
        toiletToSave.setPlace(place);
        toiletToSave.setGender(Gender.UNISEX);
        toiletToSave.setHasAccessible(true);
        toiletToSave.setHasBabyChanger(false);
        toiletToSave.setHasShower(true);

        UUID toiletId = UUID.randomUUID();
        Toilet savedToilet = new Toilet();
        savedToilet.setId(toiletId);
        savedToilet.setPlace(place);
        savedToilet.setGender(Gender.UNISEX);
        savedToilet.setHasAccessible(true);
        savedToilet.setHasBabyChanger(false);
        savedToilet.setHasShower(true);
        savedToilet.setStatus(Status.PENDING);

        PlaceResponseDTO placeDTO = new PlaceResponseDTO();
        placeDTO.setId(placeId);
        placeDTO.setName("Posto A");
        placeDTO.setAddress("Av. Central, 1000");
        placeDTO.setLatitude(-27.12345);
        placeDTO.setLongitude(-48.98765);
        placeDTO.setGooglePlaceId("some-google-place-id");
        placeDTO.setStatus(Status.APPROVED);

        ToiletResponseDTO responseDTO = new ToiletResponseDTO();
        responseDTO.setId(toiletId);
        responseDTO.setPlace(placeDTO);
        responseDTO.setGender(Gender.UNISEX);
        responseDTO.setHasAccessible(true);
        responseDTO.setHasBabyChanger(false);
        responseDTO.setHasShower(true);
        responseDTO.setStatus(Status.PENDING);

        when(placeService.findById(placeId)).thenReturn(place);
        when(toiletMapper.toEntityFromCommandDTO(dto, place)).thenReturn(toiletToSave);
        when(toiletRepository.save(toiletToSave)).thenReturn(savedToilet);
        when(toiletMapper.toResponseDTO(savedToilet)).thenReturn(responseDTO);

        ToiletResponseDTO result = toiletService.createToilet(dto);

        assertEquals(toiletId, result.getId());
        assertEquals(Gender.UNISEX, result.getGender());
        assertEquals("Posto A", result.getPlace().getName());

        verify(placeService).findById(placeId);
        verify(toiletMapper).toEntityFromCommandDTO(dto, place);
        verify(toiletRepository).save(toiletToSave);
        verify(toiletMapper).toResponseDTO(savedToilet);
    }

    @Test
    void updateToilet_shouldUpdateAndReturnToilet() {
        UUID toiletId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();

        ToiletCommandDTO dto = new ToiletCommandDTO();
        dto.setId(toiletId);
        dto.setPlaceId(placeId);
        dto.setGender(Gender.FEMALE);
        dto.setHasAccessible(false);
        dto.setHasBabyChanger(true);
        dto.setHasShower(false);

        Toilet existing = new Toilet();
        existing.setId(toiletId);

        Toilet updated = new Toilet();
        updated.setId(toiletId);
        updated.setGender(Gender.FEMALE);
        updated.setHasAccessible(false);
        updated.setHasBabyChanger(true);
        updated.setHasShower(false);
        updated.setStatus(Status.APPROVED);

        Place place = new Place();
        place.setId(placeId);
        place.setName("Posto Updated");
        place.setAddress("Rua Updated, 100");
        place.setLatitude(-28.0);
        place.setLongitude(-49.0);
        place.setGooglePlaceId("google-updated");
        place.setStatus(Status.APPROVED);

        PlaceResponseDTO placeDTO = new PlaceResponseDTO();
        placeDTO.setId(placeId);
        placeDTO.setName("Posto Updated");
        placeDTO.setAddress("Rua Updated, 100");
        placeDTO.setLatitude(-28.0);
        placeDTO.setLongitude(-49.0);
        placeDTO.setGooglePlaceId("google-updated");
        placeDTO.setStatus(Status.APPROVED);

        ToiletResponseDTO responseDTO = new ToiletResponseDTO();
        responseDTO.setId(toiletId);
        responseDTO.setPlace(placeDTO);
        responseDTO.setGender(Gender.FEMALE);
        responseDTO.setHasAccessible(false);
        responseDTO.setHasBabyChanger(true);
        responseDTO.setHasShower(false);
        responseDTO.setStatus(Status.APPROVED);

        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            ToiletCommandDTO dtoArg = invocation.getArgument(0);
            Toilet entityArg = invocation.getArgument(1);
            entityArg.setGender(dtoArg.getGender());
            entityArg.setHasAccessible(dtoArg.getHasAccessible());
            entityArg.setHasBabyChanger(dtoArg.getHasBabyChanger());
            entityArg.setHasShower(dtoArg.getHasShower());
            return null;
        }).when(toiletMapper).updateEntityFromCommandDTO(dto, existing);

        when(toiletRepository.save(existing)).thenReturn(updated);
        when(toiletMapper.toResponseDTO(updated)).thenReturn(responseDTO);

        ToiletResponseDTO result = toiletService.updateToilet(dto);

        assertEquals(toiletId, result.getId());
        assertEquals(Gender.FEMALE, result.getGender());
        assertEquals("Posto Updated", result.getPlace().getName());

        verify(toiletRepository).findById(toiletId);
        verify(toiletMapper).updateEntityFromCommandDTO(dto, existing);
        verify(toiletRepository).save(existing);
        verify(toiletMapper).toResponseDTO(updated);
    }

    @Test
    void deleteToilet_shouldDeleteAndReturnEmptyResponse() {
        UUID toiletId = UUID.randomUUID();

        Toilet toilet = new Toilet();
        toilet.setId(toiletId);

        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toilet));
        doNothing().when(toiletRepository).deleteById(toiletId);

        ToiletResponseDTO result = toiletService.deleteToilet(toiletId);

        assertNotNull(result);
        verify(toiletRepository).findById(toiletId);
        verify(toiletRepository).deleteById(toiletId);
    }
}

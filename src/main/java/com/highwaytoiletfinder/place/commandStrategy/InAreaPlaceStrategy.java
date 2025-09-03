package com.highwaytoiletfinder.place.commandStrategy;

import com.highwaytoiletfinder.place.dto.request.InAreaRequestDTO;
import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.dto.response.PlacesInAreaResponseDTO;
import com.highwaytoiletfinder.place.mapper.PlaceMapper;
import com.highwaytoiletfinder.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InAreaPlaceStrategy implements PlaceCommandStrategy {

    private final PlaceService placeService;
    private final PlaceMapper placeMapper;

    @Override
    public boolean supports(String command) {
        return "InArea".equalsIgnoreCase(command);
    }

    @Override
    public PlacesInAreaResponseDTO execute(PlaceCommandDTO dto) {
        InAreaRequestDTO inAreaDTO = dto.getInAreaRequest();

        List<PlaceResponseDTO> places = placeService.getPlacesInArea(
                        inAreaDTO.getMinLat(),
                        inAreaDTO.getMaxLat(),
                        inAreaDTO.getMinLng(),
                        inAreaDTO.getMaxLng(),
                        inAreaDTO.getLimit()
                ).stream()
                .map(placeMapper::toResponseDTO)
                .toList();

        return PlacesInAreaResponseDTO.builder()
                .places(places)
                .build();
    }
}

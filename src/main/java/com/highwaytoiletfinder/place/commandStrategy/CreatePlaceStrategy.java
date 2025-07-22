package com.highwaytoiletfinder.place.commandStrategy;

import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePlaceStrategy implements PlaceCommandStrategy {

    private final PlaceService placeService;

    @Override
    public boolean supports(String command) {
        return "create".equalsIgnoreCase(command);
    }

    @Override
    public PlaceResponseDTO execute(PlaceCommandDTO dto) {
        return placeService.createPlace(dto);
    }
}

package com.highwaytoiletfinder.place.commandStrategy;

import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;

public interface PlaceCommandStrategy {
    boolean supports(String command);
    PlaceResponseDTO execute(PlaceCommandDTO dto);
}
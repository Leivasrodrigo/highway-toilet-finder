package com.highwaytoiletfinder.place.commandStrategy;

import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;

public interface PlaceCommandStrategy<T> {
    boolean supports(String command);
    T execute(PlaceCommandDTO dto);
}
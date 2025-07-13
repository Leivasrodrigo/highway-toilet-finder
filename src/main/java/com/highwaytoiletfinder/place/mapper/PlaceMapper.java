package com.highwaytoiletfinder.place.mapper;

import com.highwaytoiletfinder.place.dto.request.PlaceRequestDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.model.Place;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper {

    public Place toEntity(PlaceRequestDTO dto) {
        return Place.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

        public PlaceResponseDTO toResponseDTO(Place place) {
        if (place == null) {
            return null;
        }

        return PlaceResponseDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .googlePlaceId(place.getGooglePlaceId())
                .status(place.getStatus())
                .build();
    }
}

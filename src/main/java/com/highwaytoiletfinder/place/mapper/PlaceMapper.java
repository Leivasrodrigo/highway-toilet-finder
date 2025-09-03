package com.highwaytoiletfinder.place.mapper;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.model.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceMapper {

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
                .build();
    }

    public void updateEntityFromCommandDTO(PlaceCommandDTO dto, Place place) {
        if (dto.getName() != null) place.setName(dto.getName());
        if (dto.getAddress() != null) place.setAddress(dto.getAddress());
        if (dto.getLatitude() != null) place.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) place.setLongitude(dto.getLongitude());
        if (dto.getGooglePlaceId() != null) place.setGooglePlaceId(dto.getGooglePlaceId());
    }

    public Place toEntityFromCommandDTO(PlaceCommandDTO dto) {
        return Place.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .googlePlaceId(dto.getGooglePlaceId())
                .build();
    }
}
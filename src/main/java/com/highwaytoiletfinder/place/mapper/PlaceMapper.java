package com.highwaytoiletfinder.place.mapper;

import com.highwaytoiletfinder.place.dto.request.PlaceRequestDTO;
import com.highwaytoiletfinder.place.dto.request.PlaceUpdateRequestDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.model.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceMapper {

    public Place toEntity(PlaceRequestDTO dto) {
        return Place.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .googlePlaceId(dto.getGooglePlaceId())
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

    public void updateEntityFromDTO(PlaceUpdateRequestDTO dto, Place place) {
        if (dto.getName() != null) place.setName(dto.getName());
        if (dto.getAddress() != null) place.setAddress(dto.getAddress());
        if (dto.getLatitude() != null) place.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) place.setLongitude(dto.getLongitude());
        if (dto.getStatus() != null) place.setStatus(dto.getStatus());

        if (dto.getGooglePlaceId() != null && place.getGooglePlaceId() == null) {
            place.setGooglePlaceId(dto.getGooglePlaceId());
        }
    }
}

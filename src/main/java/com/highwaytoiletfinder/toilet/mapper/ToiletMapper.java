package com.highwaytoiletfinder.toilet.mapper;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.toilet.dto.request.ToiletRequestDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.place.mapper.PlaceMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToiletMapper {

    private final PlaceMapper placeMapper;

    public Toilet toEntity(ToiletRequestDTO dto, Place place) {
        return Toilet.builder()
                .place(place)
                .gender(dto.getGender())
                .hasAccessible(dto.getHasAccessible())
                .hasBabyChanger(dto.getHasBabyChanger())
                .hasShower(dto.getHasShower())
                .status(Status.PENDING)
                .build();
    }

    public ToiletResponseDTO toResponseDTO(Toilet toilet) {
        return ToiletResponseDTO.builder()
                .id(toilet.getId())
                .gender(toilet.getGender())
                .status(toilet.getStatus())
                .hasAccessible(toilet.getHasAccessible())
                .hasBabyChanger(toilet.getHasBabyChanger())
                .hasShower(toilet.getHasShower())
                .avgRating(toilet.getAvgRating())
                .totalReviews(toilet.getTotalReviews())
                .place(placeMapper.toResponseDTO(toilet.getPlace()))
                .build();
    }
}

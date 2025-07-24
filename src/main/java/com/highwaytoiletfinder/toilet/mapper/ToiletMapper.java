package com.highwaytoiletfinder.toilet.mapper;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.mapper.ReviewMapper;
import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.place.mapper.PlaceMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ToiletMapper {

    private final PlaceMapper placeMapper;
    private final ReviewMapper reviewMapper;

    public ToiletResponseDTO toResponseDTO(Toilet toilet) {
        List<ReviewResponseDTO> reviewDTOs = Optional.ofNullable(toilet.getReviews())
                .orElse(List.of())
                .stream()
                .map(reviewMapper::toResponseDTO)
                .toList();

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
                .reviews(reviewDTOs)
                .build();
    }

    public void updateEntityFromCommandDTO(ToiletCommandDTO dto, Toilet toilet) {
        if (dto.getGender() != null) toilet.setGender(dto.getGender());
        if (dto.getHasShower() != null) toilet.setHasShower(dto.getHasShower());
        if (dto.getHasAccessible() != null) toilet.setHasAccessible(dto.getHasAccessible());
        if (dto.getHasBabyChanger() != null) toilet.setHasBabyChanger(dto.getHasBabyChanger());
        if (dto.getStatus() != null) toilet.setStatus(dto.getStatus());
    }

    public Toilet toEntityFromCommandDTO(ToiletCommandDTO dto, Place place) {
        return Toilet.builder()
                .gender(dto.getGender())
                .hasShower(dto.getHasShower())
                .hasBabyChanger(dto.getHasBabyChanger())
                .hasAccessible(dto.getHasAccessible())
                .status(dto.getStatus() != null ? dto.getStatus() : Status.PENDING)
                .place(place)
                .build();
    }
}

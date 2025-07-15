package com.highwaytoiletfinder.toilet.mapper;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.mapper.ReviewMapper;
import com.highwaytoiletfinder.toilet.dto.request.ToiletRequestDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.place.mapper.PlaceMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToiletMapper {

    private final PlaceMapper placeMapper;
    private final ReviewMapper reviewMapper;

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
}

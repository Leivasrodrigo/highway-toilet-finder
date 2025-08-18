package com.highwaytoiletfinder.toilet.dto.response;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.enums.Price;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToiletResponseDTO {
    private UUID id;
    private Gender gender;
    private Boolean hasAccessible;
    private Boolean hasBabyChanger;
    private Boolean hasShower;

    private Double avgRating;
    private Integer totalReviews;

    private Status status;

    private Price price;

    private PlaceResponseDTO place;

    private List<ReviewResponseDTO> reviews;
}

package com.highwaytoiletfinder.toilet.dto.response;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.toilet.enums.Gender;
import lombok.*;

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

    private PlaceResponseDTO place;
}

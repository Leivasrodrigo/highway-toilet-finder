package com.highwaytoiletfinder.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequestDTO {

    @Min(1)
    @Max(5)
    private Integer ratingGeneral;

    @Min(1)
    @Max(5)
    private Integer ratingCleanliness;

    @Min(1)
    @Max(5)
    private Integer ratingMaintenance;

    private String comment;
}

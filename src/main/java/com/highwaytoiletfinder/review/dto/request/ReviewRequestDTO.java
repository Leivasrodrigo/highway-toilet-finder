package com.highwaytoiletfinder.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDTO {
    @NotNull
    private UUID toiletId;

    @NotNull
    private UUID userId;

    @Min(1)
    @Max(5)
    private int ratingGeneral;

    @Min(1)
    @Max(5)
    private int ratingCleanliness;

    @Min(1)
    @Max(5)
    private int ratingMaintenance;

    private String comment;
}

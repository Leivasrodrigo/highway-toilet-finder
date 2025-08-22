package com.highwaytoiletfinder.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCommandDTO {

    private UUID id;

    @NotBlank(message = "command is required and must be 'create', 'update' or 'delete'")
    private String command;

    private UUID toiletId;

    private UUID userId;

    @Min(1)
    @Max(5)
    private Integer ratingCleanliness;

    @Min(1)
    @Max(5)
    private Integer ratingMaintenance;

    private String comment;
}

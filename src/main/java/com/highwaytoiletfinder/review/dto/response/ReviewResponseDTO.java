package com.highwaytoiletfinder.review.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private UUID id;
    private Double ratingGeneral;
    private int ratingCleanliness;
    private int ratingMaintenance;
    private String comment;

    private UUID userId;
    private String userName;

    private UUID toiletId;
}

package com.highwaytoiletfinder.review.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private UUID id;
    private int ratingGeneral;
    private int ratingCleanliness;
    private int ratingMaintenance;
    private String comment;

    private UUID userId;
    private String userName;

    private UUID toiletId;
}

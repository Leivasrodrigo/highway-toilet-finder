package com.highwaytoiletfinder.place.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.highwaytoiletfinder.common.enums.Status;
import lombok.*;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponseDTO {
    private UUID id;

    private String name;

    private String address;

    private Double latitude;

    private Double longitude;

    private String googlePlaceId;
}

package com.highwaytoiletfinder.place.dto.response;

import com.highwaytoiletfinder.common.enums.Status;
import lombok.*;

import java.util.UUID;

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

    private Status status;
}

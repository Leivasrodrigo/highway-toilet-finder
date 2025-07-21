package com.highwaytoiletfinder.place.dto.request;

import com.highwaytoiletfinder.common.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceUpdateRequestDTO {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String googlePlaceId;
    private Status status;
}

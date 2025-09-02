package com.highwaytoiletfinder.place.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InAreaRequestDTO {
    private double minLat;
    private double maxLat;
    private double minLng;
    private double maxLng;
    private Integer limit = 200;
}

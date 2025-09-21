package com.highwaytoiletfinder.placeImport;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceImportGridRequest {
    private double latMin;
    private double latMax;
    private double lngMin;
    private double lngMax;
    private int radius;
    private String type;
    private String keyword;
}

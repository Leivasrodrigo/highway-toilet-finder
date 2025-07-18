package com.highwaytoiletfinder.googleplaces.model;

import lombok.Data;

@Data
public class NearbySearchRequest {
    private String location;
    private int radius;
    private String keyword;
    private String type;
}

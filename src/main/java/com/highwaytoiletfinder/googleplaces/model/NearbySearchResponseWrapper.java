package com.highwaytoiletfinder.googleplaces.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbySearchResponseWrapper {
    private String status;
    private String errorMessage;
    private Object results;
}

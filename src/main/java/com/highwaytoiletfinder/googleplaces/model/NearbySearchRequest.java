package com.highwaytoiletfinder.googleplaces.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NearbySearchRequest {
    private String location;
    private int radius;
    private String keyword;
    private String type;
    @JsonProperty("next_page_token")
    private String nextPageToken;
}

package com.highwaytoiletfinder.googleplaces.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbySearchResponse {

    private List<Result> results;

    private String status;

    @JsonProperty("error_message")
    private String errorMessage;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String name;
        private Geometry geometry;
        private String place_id;
        private String vicinity;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Geometry {
            private Location location;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Location {
                private double lat;
                private double lng;
            }
        }
    }
}

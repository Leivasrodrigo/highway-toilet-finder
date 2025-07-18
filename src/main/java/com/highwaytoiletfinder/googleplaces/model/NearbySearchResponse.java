package com.highwaytoiletfinder.googleplaces.model;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbySearchResponse {
    private List<Result> results;

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

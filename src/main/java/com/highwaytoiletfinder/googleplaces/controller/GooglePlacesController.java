package com.highwaytoiletfinder.googleplaces.controller;

import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponse;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponseWrapper;
import com.highwaytoiletfinder.googleplaces.service.GooglePlacesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/googleplaces")
@RequiredArgsConstructor
public class GooglePlacesController {

    private final GooglePlacesService service;

    @PostMapping("/nearbysearch")
    public ResponseEntity<NearbySearchResponseWrapper> searchNearby(@RequestBody NearbySearchRequest request) {
        NearbySearchResponse googleResponse = service.searchNearby(request);

        NearbySearchResponseWrapper wrappedResponse = new NearbySearchResponseWrapper(
                googleResponse.getStatus(),
                googleResponse.getErrorMessage(),
                googleResponse.getResults());

        return ResponseEntity.ok(wrappedResponse);
    }
}

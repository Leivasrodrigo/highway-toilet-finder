package com.highwaytoiletfinder.googleplaces.controller;

import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponse;
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
    public ResponseEntity<NearbySearchResponse> searchNearby(@RequestBody NearbySearchRequest request) {
        return ResponseEntity.ok(service.searchNearby(request));
    }
}

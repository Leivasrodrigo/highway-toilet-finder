package com.highwaytoiletfinder.placeImport.controller;

import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.placeImport.service.PlaceImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class PlaceImportController {
    private final PlaceImportService placeImportService;

    @PostMapping("/places")
    public ResponseEntity<Void> importPlaces(@RequestBody NearbySearchRequest request) {
        placeImportService.importNearbyPlaces(request); return ResponseEntity.ok().build();
    }
}

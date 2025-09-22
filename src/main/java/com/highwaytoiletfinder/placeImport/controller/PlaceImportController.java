package com.highwaytoiletfinder.placeImport.controller;

import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.placeImport.dto.PlaceImportGridRequest;
import com.highwaytoiletfinder.placeImport.service.NearbyPlacesService;
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
    private final NearbyPlacesService nearbyPlacesService;

    @PostMapping("/places")
    public ResponseEntity<Void> importPlaces(@RequestBody NearbySearchRequest request) {
        nearbyPlacesService.importNearbyPlaces(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/import-grid")
    public ResponseEntity<String> importGrid(@RequestBody PlaceImportGridRequest request) {
        placeImportService.importPlacesInGrid(request);
        return ResponseEntity.ok("Import started for the selected grid area.");
    }
}

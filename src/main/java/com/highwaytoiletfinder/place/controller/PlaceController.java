package com.highwaytoiletfinder.place.controller;

import com.highwaytoiletfinder.place.service.PlaceService;
import com.highwaytoiletfinder.place.dto.request.PlaceRequestDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceResponseDTO>> getAll() {
        return ResponseEntity.ok(placeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponseDTO> getById(@PathVariable UUID id) {
        return placeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlaceResponseDTO> create(@RequestBody @Valid PlaceRequestDTO requestDTO) {
        PlaceResponseDTO savedPlace = placeService.save(requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPlace.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedPlace);
    }
}

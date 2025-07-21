package com.highwaytoiletfinder.place.controller;

import com.highwaytoiletfinder.place.dto.request.PlaceUpdateRequestDTO;
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
        PlaceResponseDTO response = placeService.getById(id);
        return ResponseEntity.ok(response);
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

    @PatchMapping("/{id}")
    public ResponseEntity<PlaceResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid PlaceUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(placeService.update(id, requestDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        placeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

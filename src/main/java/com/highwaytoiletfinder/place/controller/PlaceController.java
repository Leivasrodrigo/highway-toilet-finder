package com.highwaytoiletfinder.place.controller;

import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.service.PlaceService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        placeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PlaceResponseDTO> createOrUpdate(@RequestBody @Valid PlaceCommandDTO commandDTO) {
        PlaceResponseDTO result;

        if (commandDTO.getId() == null) {
            result = placeService.createPlace(commandDTO);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(result.getId())
                    .toUri();

            return ResponseEntity.created(location).body(result);
        } else {
            result = placeService.updatePlace(commandDTO);
            return ResponseEntity.ok(result);
        }
    }
}

package com.highwaytoiletfinder.place.controller;

import com.highwaytoiletfinder.place.commandStrategy.PlaceCommandStrategies;
import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.service.PlaceService;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final PlaceCommandStrategies placeCommandStrategies;
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
    public ResponseEntity<?> handlePlaceCommand(@RequestBody @Valid PlaceCommandDTO commandDTO) {
        Object result = placeCommandStrategies.execute(commandDTO.getCommand(), commandDTO);

        if ("create".equalsIgnoreCase(commandDTO.getCommand()) && result instanceof PlaceResponseDTO place) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(place.getId())
                    .toUri();
            return ResponseEntity.created(location).body(place);
        } else if ("delete".equalsIgnoreCase(commandDTO.getCommand())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
}

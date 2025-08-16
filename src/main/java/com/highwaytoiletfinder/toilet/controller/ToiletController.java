package com.highwaytoiletfinder.toilet.controller;

import com.highwaytoiletfinder.toilet.commandStrategy.ToiletCommandStrategies;
import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.service.ToiletService;
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
@RequestMapping("/api/toilets")
@RequiredArgsConstructor
public class ToiletController {

    @Autowired
    private final ToiletCommandStrategies toiletCommandStrategies;

    private final ToiletService toiletService;

    @GetMapping
    public ResponseEntity<List<ToiletResponseDTO>> getAll() {
        return ResponseEntity.ok(toiletService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToiletResponseDTO> getById(@PathVariable UUID id) {
        ToiletResponseDTO response = toiletService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ToiletResponseDTO> handleToiletCommand(@RequestBody @Valid ToiletCommandDTO commandDTO) {
        ToiletResponseDTO result = toiletCommandStrategies.execute(commandDTO.getCommand(), commandDTO);

        if ("create".equalsIgnoreCase(commandDTO.getCommand())) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(result.getId())
                    .toUri();
            return ResponseEntity.created(location).body(result);
        } else if ("delete".equalsIgnoreCase(commandDTO.getCommand())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
}

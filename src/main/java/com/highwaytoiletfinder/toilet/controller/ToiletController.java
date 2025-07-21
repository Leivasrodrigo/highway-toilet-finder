package com.highwaytoiletfinder.toilet.controller;

import com.highwaytoiletfinder.toilet.dto.request.ToiletRequestDTO;
import com.highwaytoiletfinder.toilet.dto.request.ToiletUpdateRequestDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ToiletResponseDTO> create(@RequestBody @Valid ToiletRequestDTO requestDTO) {
        ToiletResponseDTO savedToilet = toiletService.save(requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedToilet.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedToilet);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ToiletResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid ToiletUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(toiletService.update(id, requestDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        toiletService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.highwaytoiletfinder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<List<Toilet>> getAll() {
        return ResponseEntity.ok(toiletService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Toilet> getById(@PathVariable UUID id) {
        return toiletService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Toilet> create(@RequestBody @Valid Toilet toilet) {
        Toilet savedToilet = toiletService.save(toilet);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedToilet.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedToilet);    }
}

package com.highwaytoiletfinder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToiletService {
    private final ToiletRepository toiletRepository;

    public List<Toilet> getAll() {
        return toiletRepository.findAll();
    }

    public Optional<Toilet> getById(UUID id) {
        return toiletRepository.findById(id);
    }

    public Toilet save(Toilet toilet) {
        return toiletRepository.save(toilet);
    }
}

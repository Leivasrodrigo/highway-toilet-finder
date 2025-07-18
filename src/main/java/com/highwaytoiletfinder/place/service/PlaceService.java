package com.highwaytoiletfinder.place.service;

import com.highwaytoiletfinder.place.dto.request.PlaceRequestDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.mapper.PlaceMapper;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceMapper placeMapper;
    private final PlaceRepository placeRepository;

    public List<PlaceResponseDTO> getAll() {
        List<Place> places = placeRepository.findAll();
        return places.stream()
                .map(placeMapper::toResponseDTO)
                .toList();
    }

    public Optional<PlaceResponseDTO> getById(UUID id) {
        return placeRepository.findById(id)
                .map(placeMapper::toResponseDTO);
    }

    public PlaceResponseDTO save(PlaceRequestDTO dto) {

        Place place = placeMapper.toEntity(dto);
        Place saved = placeRepository.save(place);

        return placeMapper.toResponseDTO(saved);
    }
}

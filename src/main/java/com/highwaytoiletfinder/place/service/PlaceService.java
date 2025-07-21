package com.highwaytoiletfinder.place.service;

import com.highwaytoiletfinder.place.dto.request.PlaceRequestDTO;
import com.highwaytoiletfinder.place.dto.request.PlaceUpdateRequestDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.mapper.PlaceMapper;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.common.exceptions.PlaceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public PlaceResponseDTO getById(UUID id) {
        return placeRepository.findById(id)
                .map(placeMapper::toResponseDTO)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with id: " + id));
    }

    public PlaceResponseDTO save(PlaceRequestDTO dto) {

        Place place = placeMapper.toEntity(dto);
        Place saved = placeRepository.save(place);

        return placeMapper.toResponseDTO(saved);
    }

    public PlaceResponseDTO update(UUID id, PlaceUpdateRequestDTO dto) {
        Place existing = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found"));

        placeMapper.updateEntityFromDTO(dto, existing);

        Place updated = placeRepository.save(existing);
        return placeMapper.toResponseDTO(updated);
    }

    public void delete(UUID id) {
        if (!placeRepository.existsById(id)) {
            throw new PlaceNotFoundException("Place not found");
        }

        placeRepository.deleteById(id);
    }
}

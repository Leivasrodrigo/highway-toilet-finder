package com.highwaytoiletfinder.place.service;

import com.highwaytoiletfinder.common.exceptions.UserNotFoundException;
import com.highwaytoiletfinder.place.dto.request.PlaceCommandDTO;
import com.highwaytoiletfinder.place.dto.response.PlaceResponseDTO;
import com.highwaytoiletfinder.place.mapper.PlaceMapper;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.common.exceptions.PlaceNotFoundException;

import com.highwaytoiletfinder.user.model.User;
import jakarta.transaction.Transactional;
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
        System.out.println("Total places in DB: " + places.size());
        return places.stream()
                .map(placeMapper::toResponseDTO)
                .toList();
    }

    public PlaceResponseDTO getById(UUID id) {
        return placeRepository.findById(id)
                .map(placeMapper::toResponseDTO)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with id: " + id));
    }

    @Transactional
    public PlaceResponseDTO createPlace(PlaceCommandDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided for creation");
        }

        Place place = placeMapper.toEntityFromCommandDTO(dto);
        Place saved = placeRepository.save(place);
        return placeMapper.toResponseDTO(saved);
    }

    @Transactional
    public PlaceResponseDTO updatePlace(PlaceCommandDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID must be provided for update");
        }

        Place existing = placeRepository.findById(dto.getId())
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with id: " + dto.getId()));

        placeMapper.updateEntityFromCommandDTO(dto, existing);

        Place updated = placeRepository.save(existing);
        return placeMapper.toResponseDTO(updated);
    }

    @Transactional
    public PlaceResponseDTO deletePlace(UUID id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with id: " + id));

        placeRepository.deleteById(id);
        return new PlaceResponseDTO();
    }

    public Place findById(UUID id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with id: " + id));
    }
}

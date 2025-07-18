package com.highwaytoiletfinder.toilet.service;

import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.toilet.dto.request.ToiletRequestDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.mapper.ToiletMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToiletService {
    private final ToiletMapper toiletMapper;
    private final ToiletRepository toiletRepository;
    private final PlaceRepository placeRepository;

    public List<ToiletResponseDTO> getAll() {
        List<Toilet> toilets = toiletRepository.findAll();
        return toilets.stream()
                .map(toiletMapper::toResponseDTO)
                .toList();    }

    public Optional<ToiletResponseDTO> getById(UUID id) {
        return toiletRepository.findById(id)
                .map(toiletMapper::toResponseDTO);    }

    public ToiletResponseDTO save(ToiletRequestDTO dto) {
        UUID placeId = dto.getPlaceId();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + placeId));

        Toilet toilet = toiletMapper.toEntity(dto, place);
        Toilet saved = toiletRepository.save(toilet);

        return toiletMapper.toResponseDTO(saved);
    }
}

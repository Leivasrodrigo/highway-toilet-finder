package com.highwaytoiletfinder.toilet.service;

import com.highwaytoiletfinder.common.exceptions.PlaceNotFoundException;
import com.highwaytoiletfinder.common.exceptions.ToiletNotFoundException;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.toilet.dto.request.ToiletRequestDTO;
import com.highwaytoiletfinder.toilet.dto.request.ToiletUpdateRequestDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.mapper.ToiletMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import jakarta.transaction.Transactional;
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

    public ToiletResponseDTO getById(UUID id) {
        return toiletRepository.findById(id)
                .map(toiletMapper::toResponseDTO)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + id));
    }

    @Transactional
    public ToiletResponseDTO save(ToiletRequestDTO dto) {
        UUID placeId = dto.getPlaceId();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + placeId));

        Toilet toilet = toiletMapper.toEntity(dto, place);
        Toilet saved = toiletRepository.save(toilet);

        return toiletMapper.toResponseDTO(saved);
    }

    @Transactional
    public ToiletResponseDTO update(UUID id, ToiletUpdateRequestDTO dto) {
        Toilet existing = toiletRepository.findById(id)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + id));

        toiletMapper.updateEntityFromDTO(dto, existing, placeRepository);

        Toilet updated = toiletRepository.save(existing);
        return toiletMapper.toResponseDTO(updated);
    }

    @Transactional
    public void delete(UUID id) {
        if (!toiletRepository.existsById(id)) {
            throw new ToiletNotFoundException("Toilet not found with id: " + id);
        }

        toiletRepository.deleteById(id);
    }
}

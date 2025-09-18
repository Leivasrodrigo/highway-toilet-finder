package com.highwaytoiletfinder.toilet.service;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.common.exceptions.ToiletNotFoundException;
import com.highwaytoiletfinder.common.security.AuthenticatedUserProvider;
import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.place.service.PlaceService;
import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.mapper.ToiletMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToiletService {
    private final PlaceService placeService;
    private final ToiletMapper toiletMapper;
    private final ToiletRepository toiletRepository;
    private final PlaceRepository placeRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

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

    public ToiletResponseDTO getByPlaceId(UUID id) {
        return toiletRepository.findByPlaceId(id)
                .map(toiletMapper::toResponseDTO)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found for place with id: " + id));
    }

    public ToiletResponseDTO createToilet(ToiletCommandDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided for creation");
        }

        User user = authenticatedUserProvider.getAuthenticatedUser();
        if (!Role.ADMIN.equals(user.getUserRole())) {
            throw new AccessDeniedException("Only admins can create toilets.");
        }

        Place place = placeService.findById(dto.getPlaceId());

        if (toiletRepository.existsByPlaceId(place.getId())) {
            throw new IllegalStateException("This place already has a toilet assigned.");
        }

        Toilet toilet = toiletMapper.toEntityFromCommandDTO(dto, place);
        Toilet saved = toiletRepository.save(toilet);

        return toiletMapper.toResponseDTO(saved);
    }

    @Transactional
    public ToiletResponseDTO updateToilet(ToiletCommandDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID must be provided for update");
        }

        Toilet existing = toiletRepository.findById(dto.getId())
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + dto.getId()));

        toiletMapper.updateEntityFromCommandDTO(dto, existing);

        Toilet updated = toiletRepository.save(existing);
        return toiletMapper.toResponseDTO(updated);
    }

    @Transactional
    public ToiletResponseDTO deleteToilet(UUID id) {
        Toilet toilet = toiletRepository.findById(id)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + id));

        User user = authenticatedUserProvider.getAuthenticatedUser();
        if (!Role.ADMIN.equals(user.getUserRole())) {
            throw new AccessDeniedException("Only admins can delete toilets.");
        }

        toiletRepository.deleteById(id);
        return new ToiletResponseDTO();
    }

    public Toilet findById(UUID id) {
        return toiletRepository.findById(id)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + id));
    }

    public void updateToiletStatusBasedOnReviews(UUID toiletId) {
        Toilet toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found"));

        int totalReviews = toilet.getTotalReviews() != null ? toilet.getTotalReviews() : 0;

        if (totalReviews >= 5) {
            toilet.setStatus(Status.APPROVED);
        } else {
            toilet.setStatus(Status.PENDING);
        }

        toiletRepository.save(toilet);
    }

    public void updateToiletStatusBasedOnReviews(Toilet toilet) {
        int totalReviews = toilet.getTotalReviews() != null ? toilet.getTotalReviews() : 0;

        if (totalReviews >= 5) {
            toilet.setStatus(Status.APPROVED);
        } else {
            toilet.setStatus(Status.PENDING);
        }

        toiletRepository.save(toilet);
    }
}
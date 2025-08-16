package com.highwaytoiletfinder.placeImport.service;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponse;
import com.highwaytoiletfinder.googleplaces.service.GooglePlacesService;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.toilet.model.Toilet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceImportService {
    private final GooglePlacesService googlePlacesService;
    private final PlaceRepository placeRepository;
    private final ToiletRepository toiletRepository;

    public List<Place> importNearbyPlaces(NearbySearchRequest request) {
        NearbySearchResponse response = googlePlacesService.searchNearby(request);

        List<Place> newPlaces = response.getResults().stream()
                .filter(result -> !placeRepository.existsByGooglePlaceId(result.getPlace_id()))
                .map(result -> {
                    Place place = new Place();
                    place.setGooglePlaceId(result.getPlace_id());
                    place.setName(result.getName());
                    place.setAddress(result.getVicinity());
                    place.setLatitude(result.getGeometry().getLocation().getLat());
                    place.setLongitude(result.getGeometry().getLocation().getLng());
                    place.setStatus(Status.PENDING);
                    return place;
                })
                .toList();

        List<Place> savedPlaces = placeRepository.saveAll(newPlaces);

        savedPlaces.forEach(place -> {
            Toilet toilet = Toilet.builder()
                    .place(place)
                    .status(Status.PENDING)
                    .gender(null)
                    .price(null)
                    .hasShower(null)
                    .hasAccessible(null)
                    .hasBabyChanger(null)
                    .avgRating(null)
                    .totalReviews(null)
                    .build();
            toiletRepository.save(toilet);
        });

        return placeRepository.saveAll(newPlaces);
    }
}

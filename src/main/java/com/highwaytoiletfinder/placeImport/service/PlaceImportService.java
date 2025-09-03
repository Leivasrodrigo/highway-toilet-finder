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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PlaceImportService {
    private final GooglePlacesService googlePlacesService;
    private final PlaceRepository placeRepository;
    private final ToiletRepository toiletRepository;

    @Async("taskExecutor")
    public void importNearbyPlaces(NearbySearchRequest request) {
        NearbySearchResponse response = googlePlacesService.searchNearby(request);
        System.out.println("Google Places retornou " + response.getResults().size() + " resultados.");

        List<Place> newPlaces = response.getResults().stream()
                .filter(result -> !placeRepository.existsByGooglePlaceId(result.getPlace_id()))
                .map(result -> {
                    Place place = new Place();
                    place.setGooglePlaceId(result.getPlace_id());
                    place.setName(result.getName());
                    place.setAddress(result.getVicinity());
                    place.setLatitude(result.getGeometry().getLocation().getLat());
                    place.setLongitude(result.getGeometry().getLocation().getLng());
                    return place;
                })
                .toList();

        if (newPlaces.isEmpty()) {
            System.out.println("Todos os places já existem no banco.");
        }

        List<Place> savedPlaces = placeRepository.saveAll(newPlaces);
        System.out.println(savedPlaces.size() + " places foram salvos no banco.");

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
        System.out.println(savedPlaces.size() + " toilets foram criados para os novos places.");
    }
}

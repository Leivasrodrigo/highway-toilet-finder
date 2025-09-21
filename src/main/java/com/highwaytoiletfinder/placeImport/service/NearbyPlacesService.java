package com.highwaytoiletfinder.placeImport.service;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponse;
import com.highwaytoiletfinder.googleplaces.service.GooglePlacesService;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NearbyPlacesService {

    private final GooglePlacesService googlePlacesService;
    private final PlaceRepository placeRepository;
    private final ToiletRepository toiletRepository;

    @Async("taskExecutor")
    public void importNearbyPlaces(NearbySearchRequest request) {
        request.setNextPageToken(null);

        String nextPageToken = null;

        do {
            if (nextPageToken != null) {
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                request.setNextPageToken(nextPageToken);
            }

            NearbySearchResponse response = googlePlacesService.searchNearby(request);
            log.info("Google Places returned {} results.", response.getResults().size());

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

            if (!newPlaces.isEmpty()) {
                List<Place> savedPlaces = placeRepository.saveAll(newPlaces);
                savedPlaces.forEach(place -> {
                    Toilet toilet = Toilet.builder()
                            .place(place)
                            .status(Status.PENDING)
                            .build();
                    toiletRepository.save(toilet);
                });
                log.info(savedPlaces.size() + " places e toilets salvos no banco.");
            } else {
                log.info("Todos os places já existem no banco.");
            }

            nextPageToken = response.getNextPageToken();
        } while (nextPageToken != null);
    }
}

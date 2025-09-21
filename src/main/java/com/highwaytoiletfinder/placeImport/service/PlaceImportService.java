package com.highwaytoiletfinder.placeImport.service;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponse;
import com.highwaytoiletfinder.googleplaces.service.GooglePlacesService;
import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.placeImport.PlaceImportGridRequest;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.toilet.model.Toilet;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceImportService {
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
            System.out.println("Google Places returned " + response.getResults().size() + " results.");

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
                System.out.println(savedPlaces.size() + " places e toilets salvos no banco.");
            } else {
                System.out.println("Todos os places já existem no banco.");
            }

            nextPageToken = response.getNextPageToken();
        } while (nextPageToken != null);
    }

    @Async("taskExecutor")
    public void importPlacesInGrid(PlaceImportGridRequest dto) {
        double stepLat = dto.getRadius() / 111000.0;
        double stepLng = dto.getRadius() / (111000.0 * Math.cos(Math.toRadians((dto.getLatMin() + dto.getLatMax()) / 2)));

        for (double lat = dto.getLatMin(); lat <= dto.getLatMax(); lat += stepLat * 0.8) {
            for (double lng = dto.getLngMin(); lng <= dto.getLngMax(); lng += stepLng * 0.8) {
                NearbySearchRequest request = new NearbySearchRequest();
                request.setLocation(lat + "," + lng);
                request.setRadius(dto.getRadius());
                request.setType(dto.getType());
                request.setKeyword(dto.getKeyword());
                importNearbyPlaces(request);
            }
        }
    }
}

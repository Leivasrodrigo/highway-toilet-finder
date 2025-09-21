        package com.highwaytoiletfinder.placeImport.service;

        import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
        import com.highwaytoiletfinder.googleplaces.service.GooglePlacesService;
        import com.highwaytoiletfinder.place.repository.PlaceRepository;
        import com.highwaytoiletfinder.placeImport.PlaceImportGridRequest;
        import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.stereotype.Service;

        @Slf4j
        @Service
        @RequiredArgsConstructor
        public class PlaceImportService {
            private final NearbyPlacesService nearbyPlacesService;

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
                        nearbyPlacesService.importNearbyPlaces(request);
                    }
                }
            }
        }

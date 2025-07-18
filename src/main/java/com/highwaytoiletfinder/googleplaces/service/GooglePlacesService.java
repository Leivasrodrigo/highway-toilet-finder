package com.highwaytoiletfinder.googleplaces.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchRequest;
import com.highwaytoiletfinder.googleplaces.model.NearbySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class GooglePlacesService {

    @Value("${google.api.key}")
    private String apiKey;

    public NearbySearchResponse searchNearby(NearbySearchRequest req) {
        try {
            String uri = UriComponentsBuilder
                    .fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                    .queryParam("location", req.getLocation())
                    .queryParam("radius", req.getRadius())
                    .queryParam("keyword", req.getKeyword())
                    .queryParam("type", req.getType())
                    .queryParam("key", apiKey)
                    .toUriString();

            HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder json = new StringBuilder();
            while (scanner.hasNext()) {
                json.append(scanner.nextLine());
            }

            scanner.close();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json.toString(), NearbySearchResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Google Places API", e);
        }
    }
}

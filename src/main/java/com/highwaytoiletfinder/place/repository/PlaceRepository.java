package com.highwaytoiletfinder.place.repository;

import com.highwaytoiletfinder.place.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, UUID> {
    boolean existsByGooglePlaceId(String googlePlaceId);
}

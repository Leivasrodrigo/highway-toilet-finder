package com.highwaytoiletfinder;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String address;

    @Column(name = "google_place_id")
    private String googlePlaceId;
}

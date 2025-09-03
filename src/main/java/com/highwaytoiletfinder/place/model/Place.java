package com.highwaytoiletfinder.place.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.toilet.model.Toilet;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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

    private Double latitude;

    private Double longitude;

    @Column(name = "google_place_id")
    private String googlePlaceId;

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL)
    @JsonIgnore
    private Toilet toilet;
}

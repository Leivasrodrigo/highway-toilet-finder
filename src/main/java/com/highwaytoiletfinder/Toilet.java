package com.highwaytoiletfinder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "toilets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Toilet {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String placeId;

    private Boolean hasMale;
    private Boolean hasFemale;
    private Boolean hasAccessible;
    private Boolean hasBabyChanger;

    private Double avgRating;
    private Integer totalReviews;

    @OneToMany(mappedBy = "toilet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;
}

package com.highwaytoiletfinder;

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

    @OneToMany(mappedBy = "toilet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

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
}

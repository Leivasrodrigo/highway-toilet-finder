package com.highwaytoiletfinder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "has_male")
    private Boolean hasMale;
    @Column(name = "has_female")
    private Boolean hasFemale;
    @Column(name = "has_accessible")
    private Boolean hasAccessible;
    @Column(name = "has_baby_changer")
    private Boolean hasBabyChanger;

    @Column(name = "avg_rating")
    private Double avgRating;
    @Column(name = "total_reviews")
    private Integer totalReviews;

    @ManyToOne(optional = true)
    @JoinColumn(name = "place_id")
    @JsonIgnoreProperties("toilets")
    @JsonBackReference
    private Place place;

    @OneToMany(mappedBy = "toilet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;
}

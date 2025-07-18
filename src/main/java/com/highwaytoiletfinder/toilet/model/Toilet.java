package com.highwaytoiletfinder.toilet.model;

import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.toilet.enums.Gender;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "has_shower")
    private Boolean hasShower;

    @Column(name = "has_accessible")
    private Boolean hasAccessible;

    @Column(name = "has_baby_changer")
    private Boolean hasBabyChanger;

    @Column(name = "avg_rating")
    private Double avgRating;

    @Column(name = "total_reviews")
    private Integer totalReviews;

    @ManyToOne(optional = false)
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(mappedBy = "toilet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
}

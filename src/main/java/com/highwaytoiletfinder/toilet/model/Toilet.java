package com.highwaytoiletfinder.toilet.model;

import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.enums.Price;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "toilets",
uniqueConstraints = {
@UniqueConstraint(columnNames = "place_id")
    })
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
    @Column(name = "gender", nullable = true)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name= "price", nullable = true)
    private Price price;

    @Column(name = "has_shower", nullable = true)
    private Boolean hasShower;

    @Column(name = "has_accessible", nullable = true)
    private Boolean hasAccessible;

    @Column(name = "has_baby_changer", nullable = true)
    private Boolean hasBabyChanger;

    @Column(name = "avg_rating", nullable = true)
    private Double avgRating;

    @Column(name = "total_reviews", nullable = true)
    private Integer totalReviews;

    @OneToOne(optional = false)
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(mappedBy = "toilet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
}

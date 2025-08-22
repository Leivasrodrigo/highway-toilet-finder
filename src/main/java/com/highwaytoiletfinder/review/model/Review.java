package com.highwaytoiletfinder.review.model;

import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "rating_general", nullable = false)
    private Double ratingGeneral;

    @Column(name = "rating_cleanliness", nullable = false)
    private Integer ratingCleanliness;

    @Column(name = "rating_maintenance", nullable = false)
    private Integer ratingMaintenance;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(columnDefinition = "TIMESTAMP", name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "toilet_id", nullable = false)
    private Toilet toilet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

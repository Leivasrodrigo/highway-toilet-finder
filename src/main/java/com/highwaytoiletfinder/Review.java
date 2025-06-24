package com.highwaytoiletfinder;

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

    @ManyToOne
    @JoinColumn(name = "toilet_id", nullable = false)
    private Toilet toilet;

    private Integer ratingGeneral;
    private Integer ratingCleanliness;
    private Integer ratingMaintenance;

    private String comment;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant createdAt;
}

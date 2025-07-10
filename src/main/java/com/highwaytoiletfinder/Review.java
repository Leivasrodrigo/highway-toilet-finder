package com.highwaytoiletfinder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;;
    private Integer ratingGeneral;
    private Integer ratingCleanliness;
    private Integer ratingMaintenance;

    private String comment;

    @Column(columnDefinition = "TIMESTAMP", name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "toilet_id", nullable = false)
//    @JsonIgnoreProperties("reviews")
    @JsonBackReference(value = "toilet-review")
    private Toilet toilet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("reviews")
    @JsonBackReference(value = "user-review")
    private User user;
}

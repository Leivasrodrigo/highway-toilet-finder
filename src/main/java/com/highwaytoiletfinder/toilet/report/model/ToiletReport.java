package com.highwaytoiletfinder.toilet.report.model;

import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "toilet_reports",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "toilet_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToiletReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "toilet_id", nullable = false)
    private Toilet toilet;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}

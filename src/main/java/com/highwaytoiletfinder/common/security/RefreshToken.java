package com.highwaytoiletfinder.common.security;

import com.highwaytoiletfinder.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @OneToOne
    private User user;
}

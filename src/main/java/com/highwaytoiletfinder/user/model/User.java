package com.highwaytoiletfinder.user.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.highwaytoiletfinder.auth.authProvider.UserAuthProvider;
import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.review.model.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-review")
    private List<Review> reviews;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    @Builder.Default
    private Role userRole = Role.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserAuthProvider> providers = new ArrayList<>();
}

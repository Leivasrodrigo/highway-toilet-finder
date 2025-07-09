package com.highwaytoiletfinder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-review")
    private List<Review> reviews;
}

package com.highwaytoiletfinder.auth.authProvider.repository;

import com.highwaytoiletfinder.auth.authProvider.authProviderEnum.AuthProvider;
import com.highwaytoiletfinder.auth.authProvider.model.UserAuthProvider;
import com.highwaytoiletfinder.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthProviderRepository extends JpaRepository<UserAuthProvider, UUID> {
    Optional<UserAuthProvider> findByUserAndProvider(User user, AuthProvider provider);

    boolean existsByUserAndProvider(User user, AuthProvider provider);
}

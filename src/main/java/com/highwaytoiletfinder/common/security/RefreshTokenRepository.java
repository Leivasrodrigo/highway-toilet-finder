package com.highwaytoiletfinder.common.security;

import com.highwaytoiletfinder.user.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

//    @Transactional
    void deleteByUser(User user);
}

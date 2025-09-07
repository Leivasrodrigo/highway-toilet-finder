package com.highwaytoiletfinder.auth.passwordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByExpiryDateBefore(LocalDateTime now);
}

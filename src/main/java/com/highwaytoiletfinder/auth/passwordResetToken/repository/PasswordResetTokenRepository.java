package com.highwaytoiletfinder.auth.passwordResetToken.repository;

import com.highwaytoiletfinder.auth.passwordResetToken.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    void deleteByPinCode    (String pinCode);
    void deleteAllByExpiryDateBefore(LocalDateTime now);
    boolean existsByPinCodeAndExpiryDateAfter(String pinCode, LocalDateTime now);
    Optional<PasswordResetToken> findByPinCode(String pinCode);
}

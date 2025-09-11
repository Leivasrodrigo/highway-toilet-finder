package com.highwaytoiletfinder.auth.passwordResetToken.cleanUp;

import com.highwaytoiletfinder.auth.passwordResetToken.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetTokenCleanupService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpiredTokens() {
        int deleted = passwordResetTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());

        if (deleted > 0) {
            log.info("AUDIT: {} expired password reset tokens deleted at {}", deleted, Instant.now());
        } else {
            log.debug("No expired password reset tokens found at {}", Instant.now());
        }
    }
}

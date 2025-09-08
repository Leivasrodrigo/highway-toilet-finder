package com.highwaytoiletfinder.auth.passwordResetToken;
import com.highwaytoiletfinder.auth.authProvider.EmailService;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import com.highwaytoiletfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final UserService userService;

    private static final int EXPIRATION_MINUTES = 30;

    @Value("${email.template.id}")
    private String TEMPLATE_ID;

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String pin;
        do {
            pin = RandomStringUtils.randomNumeric(6);
        } while (tokenRepository.existsByPinCodeAndExpiryDateAfter(pin, LocalDateTime.now()));

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .pinCode(pin)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))
                .build();
        tokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getName(), user.getEmail(), TEMPLATE_ID, pin);
    }

    public UUID validatePin(String pin) {
        PasswordResetToken resetToken = tokenRepository.findByPinCode(pin)
                .orElseThrow(() -> new RuntimeException("Invalid or expired PIN"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("PIN expired");
        }

        return resetToken.getId();
    }

    public void consumeTokenAndResetPassword(UUID tokenId, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token expired");
        }

        userService.resetPassword(resetToken.getUser(), newPassword);

        tokenRepository.delete(resetToken);
        tokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}

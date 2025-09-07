package com.highwaytoiletfinder.auth.passwordResetToken;
import com.highwaytoiletfinder.auth.authProvider.EmailService;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    private static final int EXPIRATION_MINUTES = 30;
    private static final String TEMPLATE_ID = "ynrw7gynd1o42k8e";

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))
                .build();
        tokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getName(), user.getEmail(), TEMPLATE_ID, token);
    }

    public User validateAndConsumeToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();

        tokenRepository.delete(resetToken);

        return user;
    }
}

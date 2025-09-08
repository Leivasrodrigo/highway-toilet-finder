package com.highwaytoiletfinder.user.commandStrategy;

import com.highwaytoiletfinder.auth.commandStrategy.AuthCommandStrategy;
import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.auth.passwordResetToken.PasswordResetService;
import com.highwaytoiletfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResetPasswordStrategy implements AuthCommandStrategy {

    private final PasswordResetService passwordResetService;
    private final UserService userService;

    @Override
    public boolean supports(String command) {
        return "resetPassword".equalsIgnoreCase(command);
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO dto) {
        UUID passwordToken = dto.getPasswordToken();
        passwordResetService.consumeTokenAndResetPassword(passwordToken, dto.getPassword());

        return AuthResponseDTO.builder()
                .message("Senha redefinida com sucesso!")
                .build();
    }
}

package com.highwaytoiletfinder.auth.commandStrategy;

import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.auth.passwordResetToken.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ValidatePinStrategy implements AuthCommandStrategy {
    private final PasswordResetService passwordResetService;

    @Override
    public boolean supports(String command) {
        return "validatePin".equalsIgnoreCase(command);
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO dto) {
        UUID passwordToken = passwordResetService.validatePin(dto.getPin());

        return AuthResponseDTO.builder()
                .message("PIN validated!")
                .passwordToken(passwordToken)
                .build();
    }
}

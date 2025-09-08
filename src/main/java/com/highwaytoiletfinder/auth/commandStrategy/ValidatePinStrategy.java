package com.highwaytoiletfinder.auth.commandStrategy;

import com.highwaytoiletfinder.auth.authProvider.authProviderEnum.AuthProvider;
import com.highwaytoiletfinder.auth.authProvider.repository.UserAuthProviderRepository;
import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.auth.passwordResetToken.PasswordResetService;
import com.highwaytoiletfinder.common.security.AdminInitializer;
import com.highwaytoiletfinder.common.security.JwtUtil;
import com.highwaytoiletfinder.common.security.RefreshTokenService;
import com.highwaytoiletfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
                .message("PIN válido!")
                .passwordToken(passwordToken)
                .build();
    }
}

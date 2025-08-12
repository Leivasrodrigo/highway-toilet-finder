package com.highwaytoiletfinder.auth.commandStrategy;

import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.common.security.JwtUtil;
import com.highwaytoiletfinder.common.security.RefreshToken;
import com.highwaytoiletfinder.common.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenStrategy implements AuthCommandStrategy{

    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Override
    public boolean supports(String command) {
        return "refresh".equalsIgnoreCase(command);
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO dto) {
        String refreshTokenString = dto.getRefreshToken();

        var refreshTokenEntity = refreshTokenService.findByToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (!refreshTokenService.isValid(refreshTokenEntity)) {
            throw new RuntimeException("Refresh token expired");
        }

        String newAccessToken = jwtUtil.generateToken(refreshTokenEntity.getUser().getEmail());

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(refreshTokenEntity.getUser());

        return AuthResponseDTO.builder()
                .id(refreshTokenEntity.getUser().getId())
                .message("Token refreshed successfully")
                .token(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }
}

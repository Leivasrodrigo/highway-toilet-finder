package com.highwaytoiletfinder.auth.commandStrategy;

import com.highwaytoiletfinder.auth.authProvider.AuthProvider;
import com.highwaytoiletfinder.auth.authProvider.UserAuthProviderRepository;
import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.common.security.AdminInitializer;
import com.highwaytoiletfinder.common.security.JwtUtil;
import com.highwaytoiletfinder.common.security.RefreshTokenService;
import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;


@Component
@RequiredArgsConstructor
public class LoginStrategy implements AuthCommandStrategy {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final AdminInitializer adminInitializer;
    private final UserAuthProviderRepository userAuthProviderRepository;

    @Override
    public boolean supports(String command) {
        return "login".equalsIgnoreCase(command);
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO dto) {
        var user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean hasLocal = userAuthProviderRepository.existsByUserAndProvider(user, AuthProvider.LOCAL);
        if (!hasLocal) {
            throw new RuntimeException("This account is linked to Google. Use Google login or set a password to enable email login.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            String jwtToken = jwtUtil.generateToken(dto.getEmail());

            var prevRole = user.getUserRole();
            adminInitializer.syncAdminRole(user);
            if (prevRole != user.getUserRole()) {
                userRepository.save(user);
            }

            var refreshToken = refreshTokenService.createRefreshToken(user);

            return AuthResponseDTO.builder()
                    .message("Login successful")
                    .id(user.getId())
                    .token(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
}

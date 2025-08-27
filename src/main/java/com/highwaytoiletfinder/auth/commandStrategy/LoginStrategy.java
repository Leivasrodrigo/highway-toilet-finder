package com.highwaytoiletfinder.auth.commandStrategy;

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

    @Override
    public boolean supports(String command) {
        return "login".equalsIgnoreCase(command);
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO dto) {
        var user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isGoogleUser()) {
            throw new RuntimeException("This user must login via Google");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            String jwtToken = jwtUtil.generateToken(dto.getEmail());

            Role currentRole = user.getUserRole();

            adminInitializer.syncAdminRole(user);
            if (currentRole != user.getUserRole()) {
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

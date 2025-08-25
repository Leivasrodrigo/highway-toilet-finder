package com.highwaytoiletfinder.auth.commandStrategy;

import com.highwaytoiletfinder.auth.service.AuthService;
import com.highwaytoiletfinder.common.security.AdminInitializer;
import com.highwaytoiletfinder.common.security.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.common.security.JwtUtil;
import com.highwaytoiletfinder.common.security.RefreshTokenService;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleLoginStrategy implements AuthCommandStrategy {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AdminInitializer adminInitializer;
    private final AuthService authService;

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Override
    public boolean supports(String command) {
        return "googleLogin".equalsIgnoreCase(command);
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO dto) {
        try {
            String idTokenString = dto.getIdToken();
            System.out.println(idTokenString);
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            ).setAudience(Collections.singletonList(clientId))
                    .build();
            System.out.println("Client ID configurado: " + clientId);
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID token.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        AuthRequestDTO registerDto = new AuthRequestDTO();
                        registerDto.setEmail(email);
                        registerDto.setName(name);
                        registerDto.setPassword("GOOGLE_TEMP_PASSWORD");
                        authService.register(registerDto);
                        return userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Failed to register Google user"));
                    });

            String jwt = jwtUtil.generateToken(email);
            var refreshToken = refreshTokenService.createRefreshToken(user);

            Role currentRole = user.getUserRole();

            adminInitializer.syncAdminRole(user);
            if (currentRole != user.getUserRole()) {
                userRepository.save(user);
            }

            log.info("User '{}' logged in with role: {}", user.getEmail(), user.getUserRole());

            return AuthResponseDTO.builder()
                    .message("Login via Google successful")
                    .id(user.getId())
                    .token(jwt)
                    .refreshToken(refreshToken.getToken())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to login via Google", e);
        }
    }
}

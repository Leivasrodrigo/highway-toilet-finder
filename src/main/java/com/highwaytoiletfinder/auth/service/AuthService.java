package com.highwaytoiletfinder.auth.service;

import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.common.exceptions.EmailAlreadyInUseException;
import com.highwaytoiletfinder.common.security.AdminInitializer;
import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminInitializer adminInitializer;

    public AuthResponseDTO register(AuthRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use: " + dto.getEmail());
        }

        boolean isGoogle = "googleLogin".equalsIgnoreCase(dto.getCommand());

        User.UserBuilder userBuilder = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .userRole(Role.USER)
                .googleUser(isGoogle);

        if (!isGoogle) {
            userBuilder.passwordHash(passwordEncoder.encode(dto.getPassword()));
        } else {
            userBuilder.passwordHash(null);
        }

        User newUser = userBuilder.build();

        adminInitializer.syncAdminRole(newUser);

        userRepository.save(newUser);

        return AuthResponseDTO.builder()
                .id(newUser.getId())
                .message("User registered successfully")
                .build();
    }
}

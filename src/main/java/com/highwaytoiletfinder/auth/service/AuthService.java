package com.highwaytoiletfinder.auth.service;

import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.common.exceptions.EmailAlreadyInUseException;
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

    public AuthResponseDTO register(AuthRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use: " + dto.getEmail());
        }

        User newUser = User.builder()
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .userRole(dto.getUserRole() != null ? dto.getUserRole() : Role.USER)
                .build();

        userRepository.save(newUser);

        return AuthResponseDTO.builder()
                .id(newUser.getId())
                .message("User registered successfully")
                .build();
    }
}

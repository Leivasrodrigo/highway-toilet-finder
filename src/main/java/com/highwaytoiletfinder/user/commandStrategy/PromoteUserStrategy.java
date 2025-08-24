package com.highwaytoiletfinder.user.commandStrategy;

import com.highwaytoiletfinder.common.security.AuthenticatedUserProvider;
import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.user.dto.request.UserCommandDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import com.highwaytoiletfinder.user.mapper.UserMapper;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromoteUserStrategy implements UserCommandStrategy {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Override
    public boolean supports(String command) {
        return "promote".equalsIgnoreCase(command);
    }

    @Override
    @Transactional
    public UserResponseDTO execute(UserCommandDTO dto) {
        User actingUser = authenticatedUserProvider.getAuthenticatedUser();
        if (!Role.ADMIN.equals(actingUser.getUserRole())) {
            throw new SecurityException("Access denied: only admins can promote users.");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must be provided to perform this action.");
        }

        User target = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getEmail()));

        if (Role.ADMIN.equals(target.getUserRole())) {
            throw new IllegalStateException("Target is an admin.");
        }

        if (Role.MODERATOR.equals(target.getUserRole())) {
            throw new IllegalStateException("User " + dto.getEmail() + " is already a Moderator.");
        }

        Role previousRole = target.getUserRole();
        target.setUserRole(Role.MODERATOR);
        userRepository.save(target);

        log.info("AUDIT: User '{}' role changed from '{}' to '{}' by admin '{}'",
                target.getEmail(), previousRole, target.getUserRole(), actingUser.getEmail());

        return userMapper.toResponseDTO(target);
    }
}

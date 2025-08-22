package com.highwaytoiletfinder.common.security;

import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final AdminProperties adminProperties;
    private final UserRepository userRepository;

    @Value("${app.admin.emails}")
    private String[] defaultAdmins;

    @PostConstruct
    @Transactional
    public void initAdmins() {
        adminProperties.getEmails().forEach(email -> {
            userRepository.findByEmail(email).ifPresent(user -> {
                if (!user.getUserRole().equals(Role.ADMIN)) {
                    user.setUserRole(Role.ADMIN);
                    userRepository.save(user);
                    System.out.println("User promoted to ADMIN: " + email);
                }
            });
        });
    }

    public void syncAdminRole(User user) {
        Role previousRole = user.getUserRole();

        boolean isAdminEmail = Arrays.stream(defaultAdmins)
                .anyMatch(email -> email.equalsIgnoreCase(user.getEmail()));

        if (isAdminEmail) {
            user.setUserRole(Role.ADMIN);
        } else {
            if (!Role.MODERATOR.equals(user.getUserRole())) {
                user.setUserRole(Role.USER);
            }
        }

        if (!previousRole.equals(user.getUserRole())) {
            log.info("AUDIT: User '{}' role changed from '{}' to '{}' at {}",
                    user.getEmail(), previousRole, user.getUserRole(), Instant.now());
        }
    }
}

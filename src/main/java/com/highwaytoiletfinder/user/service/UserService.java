package com.highwaytoiletfinder.user.service;

import com.highwaytoiletfinder.common.security.PasswordValidatorUtil;
import com.highwaytoiletfinder.auth.authProvider.authProviderEnum.AuthProvider;
import com.highwaytoiletfinder.auth.authProvider.model.UserAuthProvider;
import com.highwaytoiletfinder.auth.authProvider.repository.UserAuthProviderRepository;
import com.highwaytoiletfinder.common.exceptions.UserNotFoundException;
import com.highwaytoiletfinder.common.security.AuthenticatedUserProvider;
import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.user.dto.request.UserCommandDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import com.highwaytoiletfinder.user.mapper.UserMapper;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserAuthProviderRepository userAuthProviderRepository;

    public List<UserResponseDTO> getAll() {
        User authUser = authenticatedUserProvider.getAuthenticatedUser();
        boolean isAdmin = Role.ADMIN.equals(authUser.getUserRole());

        if (!isAdmin) {
            throw new AccessDeniedException("Only admins can see all users");
        }

        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public UserResponseDTO getById(UUID id) {
        User authUser = authenticatedUserProvider.getAuthenticatedUser();
        boolean isAdmin = Role.ADMIN.equals(authUser.getUserRole());
        boolean isOwner = authUser.getId().equals(id);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You cannot view another user's profile");
        }

        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserResponseDTO updateUser(UserCommandDTO dto) {
        User authUser = authenticatedUserProvider.getAuthenticatedUser();

        UUID targetUserId = dto.getId() != null ? dto.getId() : authUser.getId();

        boolean isAdmin = Role.ADMIN.equals(authUser.getUserRole());
        boolean isOwner = authUser.getId().equals(targetUserId);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You cannot update another user's profile");
        }

        User existing = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + targetUserId));

        userMapper.updateEntityFromCommandDTO(dto, existing);

        User updated = userRepository.save(existing);
        return userMapper.toResponseDTO(updated);
    }

    @Transactional
    public UserResponseDTO updatePassword(UserCommandDTO dto) {
        User authUser = authenticatedUserProvider.getAuthenticatedUser();
        UUID targetUserId = dto.getId() != null ? dto.getId() : authUser.getId();

        boolean isOwner = authUser.getId().equals(targetUserId);
        boolean isAdmin = Role.ADMIN.equals(authUser.getUserRole());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You cannot update this user's password");
        }

        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + targetUserId));

        boolean hasPassword = user.getPasswordHash() != null;

        if (!isAdmin && hasPassword) {
            if (dto.getCurrentPassword() == null || dto.getCurrentPassword().isBlank()) {
                throw new IllegalArgumentException("Current password must be provided for verification");
            }
            if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("New password must be provided");
        }
        PasswordValidatorUtil.validate(dto.getPassword());

        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        User updated = userRepository.save(user);

        if (!userAuthProviderRepository.existsByUserAndProvider(user, AuthProvider.LOCAL)) {
            userAuthProviderRepository.save(
                    UserAuthProvider.builder()
                            .user(user)
                            .provider(AuthProvider.LOCAL)
                            .build()
            );
        }

        return userMapper.toResponseDTO(updated);
    }

    @Transactional
    public void resetPassword(User user, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password must be provided");
        }
        PasswordValidatorUtil.validate(newPassword);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        if (!userAuthProviderRepository.existsByUserAndProvider(user, AuthProvider.LOCAL)) {
            userAuthProviderRepository.save(
                    UserAuthProvider.builder()
                            .user(user)
                            .provider(AuthProvider.LOCAL)
                            .build()
            );
        }
    }

    @Transactional
    public UserResponseDTO deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        User authUser = authenticatedUserProvider.getAuthenticatedUser();
        boolean isAdmin = Role.ADMIN.equals(authUser.getUserRole());
        boolean isOwner = authUser.getId().equals(id);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You cannot delete another user's profile");
        }

        userRepository.deleteById(id);
        return new UserResponseDTO();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
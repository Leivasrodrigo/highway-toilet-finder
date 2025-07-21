package com.highwaytoiletfinder.user.service;

import com.highwaytoiletfinder.common.exceptions.UserNotFoundException;
import com.highwaytoiletfinder.user.dto.request.UserRequestDTO;
import com.highwaytoiletfinder.user.dto.request.UserUpdateRequestDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import com.highwaytoiletfinder.user.mapper.UserMapper;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public List<UserResponseDTO> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public UserResponseDTO getById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserResponseDTO save(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);

        String passwordHash = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(passwordHash);

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateRequestDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userMapper.updateEntityFromDTO(dto, existing);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            existing.setPasswordHash(hashedPassword);
        }

        User updated = userRepository.save(existing);
        return userMapper.toResponseDTO(updated);
    }

    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}

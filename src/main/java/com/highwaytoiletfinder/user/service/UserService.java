package com.highwaytoiletfinder.user.service;

import com.highwaytoiletfinder.common.exceptions.UserNotFoundException;
import com.highwaytoiletfinder.user.dto.request.UserCommandDTO;
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

    public UserResponseDTO createUser(UserCommandDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided for creation");
        }

        User user = userMapper.toEntityFromCommandDTO(dto);
        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    @Transactional
    public UserResponseDTO updateUser(UserCommandDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID must be provided for update");
        }

        User existing = userRepository.findById(dto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + dto.getId()));

        userMapper.updateEntityFromCommandDTO(dto, existing);

        User updated = userRepository.save(existing);
        return userMapper.toResponseDTO(updated);
    }

    @Transactional
    public UserResponseDTO deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.deleteById(id);
        return new UserResponseDTO();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}

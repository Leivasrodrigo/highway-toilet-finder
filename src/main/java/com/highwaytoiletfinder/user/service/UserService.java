package com.highwaytoiletfinder.user.service;

import com.highwaytoiletfinder.user.dto.request.UserRequestDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import com.highwaytoiletfinder.user.mapper.UserMapper;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Optional<UserResponseDTO> getById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO);
    }

    public UserResponseDTO save(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);

        String passwordHash = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(passwordHash);

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }
}

package com.highwaytoiletfinder.user.mapper;

import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import com.highwaytoiletfinder.review.mapper.ReviewMapper;
import com.highwaytoiletfinder.user.dto.request.UserRequestDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import com.highwaytoiletfinder.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ReviewMapper reviewMapper;

    public User toEntity(UserRequestDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserResponseDTO toResponseDTO(User user) {
        List<ReviewResponseDTO> reviewDTOs = Optional.ofNullable(user.getReviews())
                .orElse(List.of())
                .stream()
                .map(reviewMapper::toResponseDTO)
                .toList();

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .reviews(reviewDTOs)
                .build();
    }
}


package com.highwaytoiletfinder.user.dto.response;

import com.highwaytoiletfinder.common.security.Role;
import com.highwaytoiletfinder.review.dto.response.ReviewResponseDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private Role userRole;
    private List<ReviewResponseDTO> reviews;

}

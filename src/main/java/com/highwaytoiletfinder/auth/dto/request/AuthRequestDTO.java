package com.highwaytoiletfinder.auth.dto.request;

import com.highwaytoiletfinder.common.security.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDTO {
    @NotBlank(message = "Command is required")
    private String command;

    private String email;

    private String password;

    private String name;

    private String refreshToken;

    private String idToken;
}

package com.highwaytoiletfinder.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDTO {

    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;
}

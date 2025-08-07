package com.highwaytoiletfinder.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCommandDTO {

    @NotBlank(message = "Command is required and must be 'create', 'update' or 'delete'")
    private String command;

    private UUID id;

    private String name;

    private String email;

    private String password;

    private String currentPassword;
}

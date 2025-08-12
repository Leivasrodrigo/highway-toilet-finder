package com.highwaytoiletfinder.auth.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private UUID id;

    private String message;

    private String token;

    private String refreshToken;
}

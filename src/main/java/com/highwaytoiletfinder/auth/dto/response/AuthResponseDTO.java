package com.highwaytoiletfinder.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private UUID passwordToken;
}

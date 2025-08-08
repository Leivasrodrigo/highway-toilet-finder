package com.highwaytoiletfinder.auth.commandStrategy;

import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;

public interface AuthCommandStrategy {
    boolean supports(String command);
    AuthResponseDTO execute(AuthRequestDTO dto);
}

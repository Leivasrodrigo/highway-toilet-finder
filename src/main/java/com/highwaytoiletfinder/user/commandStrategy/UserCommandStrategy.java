package com.highwaytoiletfinder.user.commandStrategy;

import com.highwaytoiletfinder.user.dto.request.UserCommandDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;

public interface UserCommandStrategy {
    boolean supports(String command);
    UserResponseDTO execute(UserCommandDTO dto);
}

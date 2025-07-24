package com.highwaytoiletfinder.user.commandStrategy;

import com.highwaytoiletfinder.user.dto.request.UserCommandDTO;
import com.highwaytoiletfinder.user.dto.response.UserResponseDTO;
import com.highwaytoiletfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserStrategy implements UserCommandStrategy {

    private final UserService userService;

    @Override
    public boolean supports(String command) {
        return "delete".equalsIgnoreCase(command);
    }

    @Override
    public UserResponseDTO execute(UserCommandDTO dto) {
        return userService.deleteUser(dto.getId());
    }
}

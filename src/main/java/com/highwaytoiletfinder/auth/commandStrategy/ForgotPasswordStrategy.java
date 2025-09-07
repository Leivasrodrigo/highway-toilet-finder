package com.highwaytoiletfinder.auth.commandStrategy;
import com.highwaytoiletfinder.auth.dto.request.AuthRequestDTO;
import com.highwaytoiletfinder.auth.dto.response.AuthResponseDTO;
import com.highwaytoiletfinder.auth.passwordResetToken.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForgotPasswordStrategy implements AuthCommandStrategy {

    private final PasswordResetService passwordResetService;

    @Override
    public boolean supports(String command) {
        return "forgotPassword".equalsIgnoreCase(command);
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO dto) {
        passwordResetService.requestPasswordReset(dto.getEmail());

        return AuthResponseDTO.builder()
                .message("Se o email existir, enviamos instruções para redefinir a senha.")
                .build();
    }
}

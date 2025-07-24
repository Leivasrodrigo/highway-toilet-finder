package com.highwaytoiletfinder.toilet.commandStrategy;

import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateToiletStrategy implements ToiletCommandStrategy {

    private final ToiletService toiletService;

    @Override
    public boolean supports(String command) {
        return "update".equalsIgnoreCase(command);
    }

    @Override
    public ToiletResponseDTO execute(ToiletCommandDTO dto) {
        return toiletService.updateToilet(dto);
    }
}

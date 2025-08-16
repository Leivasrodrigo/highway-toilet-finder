package com.highwaytoiletfinder.toilet.commandStrategy;

import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.report.service.ToiletReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ToiletReactivateStrategy implements ToiletCommandStrategy{

    private final ToiletReportService toiletReportService;

    @Override
    public boolean supports(String command) {
        return "reactivate".equalsIgnoreCase(command);
    }

    @Override
    public ToiletResponseDTO execute(ToiletCommandDTO dto) {
        return toiletReportService.reactivateToilet(dto.getId());
    }
}

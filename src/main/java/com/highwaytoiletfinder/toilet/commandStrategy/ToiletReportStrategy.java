package com.highwaytoiletfinder.toilet.commandStrategy;

import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.report.service.ToiletReportService;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ToiletReportStrategy implements ToiletCommandStrategy {

    private final ToiletReportService toiletReportService;
    private final ToiletService toiletService;

    @Override
    public boolean supports(String command) {
        return "report".equalsIgnoreCase(command);
    }

    @Override
    public ToiletResponseDTO execute(ToiletCommandDTO dto) {
        return toiletReportService.reportToilet(dto.getUserId(), dto.getId());
    }
}

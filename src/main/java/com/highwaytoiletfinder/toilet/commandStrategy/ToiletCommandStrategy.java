package com.highwaytoiletfinder.toilet.commandStrategy;

import com.highwaytoiletfinder.toilet.dto.request.ToiletCommandDTO;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;

public interface ToiletCommandStrategy {
    boolean supports(String command);
    ToiletResponseDTO execute(ToiletCommandDTO dto);
}

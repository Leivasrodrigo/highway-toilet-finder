package com.highwaytoiletfinder.toilet.dto.request;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.enums.Price;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToiletCommandDTO {

    @NotBlank(message = "Command is required and must be 'create', 'update' or 'delete'")
    private String command;

    private UUID id;

    private Gender gender;

    private Boolean hasAccessible;

    private Boolean hasBabyChanger;

    private Boolean hasShower;

    private Price price;

    private Status status;

    private UUID placeId;

    private UUID userId;
}

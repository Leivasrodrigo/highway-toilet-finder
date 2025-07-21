package com.highwaytoiletfinder.toilet.dto.request;

import com.highwaytoiletfinder.toilet.enums.Gender;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToiletUpdateRequestDTO {

    private Gender gender;

    private Boolean hasShower;

    private Boolean hasAccessible;

    private Boolean hasBabyChanger;

    private UUID placeId;
}


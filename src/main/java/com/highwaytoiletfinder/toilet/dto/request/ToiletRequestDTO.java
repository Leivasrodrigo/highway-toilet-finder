package com.highwaytoiletfinder.toilet.dto.request;

import com.highwaytoiletfinder.toilet.enums.Gender;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToiletRequestDTO {
    @NonNull
    private UUID placeId;

    @NonNull
    private Gender gender;

    private Boolean hasAccessible;
    private Boolean hasBabyChanger;
    private Boolean hasShower;
}

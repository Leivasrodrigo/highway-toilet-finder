package com.highwaytoiletfinder.place.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceRequestDTO {
    @NotBlank
    private String name;

    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}

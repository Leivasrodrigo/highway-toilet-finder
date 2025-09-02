package com.highwaytoiletfinder.place.dto.request;

import com.highwaytoiletfinder.common.enums.Status;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceCommandDTO {

    @NotBlank(message = "Command is required.")
    private String command;

    private UUID id;

    private String name;

    private String address;

    private Double latitude;

    private Double longitude;

    private String googlePlaceId;

    private Status status;

    private InAreaRequestDTO inAreaRequest;
}

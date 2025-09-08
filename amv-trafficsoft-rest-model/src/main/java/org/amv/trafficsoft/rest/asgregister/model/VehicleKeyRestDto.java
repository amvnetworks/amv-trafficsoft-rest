package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleKeyRestDto.Builder.class)
@Schema(description = "A resource representing a vehicle key.")
public class VehicleKeyRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "key", description = "The KEY of the vehicle.")
    private String key;

    @Schema(name = "vehicleId", description = "The ID of the vehicle.")
    private Long vehicleId;

    @Schema(name = "valid", description = "Flag indicating whether the KEY is valid.")
    private boolean valid;
}

package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleKeyResponseRestDto.Builder.class)
@Schema(description = "A resource representing a container for vehicle key data.")
public class VehicleKeyResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "vehicleKey", description = "A resource representing a vehicle key.")
    private VehicleKeyRestDto vehicleKey;
}

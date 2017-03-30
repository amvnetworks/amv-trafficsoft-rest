package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;
@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleKeyResponseDto.Builder.class)
@ApiModel(description = "A resource representing a container for vehicle key data.")
public class VehicleKeyResponseDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(notes = "A resource representing a vehicle key.")
    private final VehicleKeyRestDto vehicleKey;
}

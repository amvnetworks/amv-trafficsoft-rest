package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleKeyRestDto.Builder.class)
@ApiModel(description = "A resource representing a vehicle key.")
public class VehicleKeyRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(notes = "The KEY of the vehicle.")
    private String key;

    @ApiModelProperty(notes = "The ID of the vehicle.")
    private Long vehicleId;

    @ApiModelProperty(notes = "Flag indicating whether the KEY is valid.")
    private boolean valid;
}

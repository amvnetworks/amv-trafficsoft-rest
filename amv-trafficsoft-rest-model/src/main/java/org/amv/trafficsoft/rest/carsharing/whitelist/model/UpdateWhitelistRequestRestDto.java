package org.amv.trafficsoft.rest.carsharing.whitelist.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = UpdateWhitelistRequestRestDto.Builder.class)
@ApiModel(description = "A resource representing the request for updating vehicleWhitelists")
public class UpdateWhitelistRequestRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(value = "A list of whitelists associated to vehicles", required = true)
    @Singular(value = "addVehicleWhitelist")
    private List<VehicleWhitelistRestDto> vehicleWhitelists;

}

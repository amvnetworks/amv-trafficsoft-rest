package org.amv.trafficsoft.rest.carsharing.whitelist.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleWhitelistRestDto.Builder.class)
public class VehicleWhitelistRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(name = "vehicleId", value = "Id of a vehicle", required = true)
    private long vehicleId;

    @ApiModelProperty(name = "whitelist", value = "A list of driver tag ids", allowEmptyValue = true)
    @Singular(value = "addWhitelist")
    private List<String> whitelist;
}

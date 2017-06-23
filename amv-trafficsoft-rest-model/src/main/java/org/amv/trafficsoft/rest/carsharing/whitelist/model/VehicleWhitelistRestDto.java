package org.amv.trafficsoft.rest.carsharing.whitelist.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleWhitelistRestDto.Builder.class)
public class VehicleWhitelistRestDto {
    @ApiModelProperty(value = "Id of a vehicle", required = true)
    private long vehicleId;

    @ApiModelProperty(value = "A list of driver tag ids", allowEmptyValue = true, example = "[545810DB,14C76FDA,040D2332FF2981]")
    @Singular(value = "addWhitelist")
    private List<String> whitelist;
}

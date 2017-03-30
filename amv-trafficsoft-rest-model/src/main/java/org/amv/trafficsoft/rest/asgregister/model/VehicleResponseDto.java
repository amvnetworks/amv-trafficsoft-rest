package org.amv.trafficsoft.rest.asgregister.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "A resource representing a container for vehicle data.")
public class VehicleResponseDto {
    @ApiModelProperty(notes = "A resource representing a vehicle.")
    private VehicleRestDto vehicle;
}

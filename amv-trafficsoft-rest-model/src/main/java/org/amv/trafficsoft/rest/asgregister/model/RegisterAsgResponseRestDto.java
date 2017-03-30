package org.amv.trafficsoft.rest.asgregister.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "A resource representing the response after initializing a vehicle with OEM/series/model data.")
public class RegisterAsgResponseRestDto {
    @ApiModelProperty(notes = "A resource representing a vehicle.")
    private VehicleRestDto vehicle;
}
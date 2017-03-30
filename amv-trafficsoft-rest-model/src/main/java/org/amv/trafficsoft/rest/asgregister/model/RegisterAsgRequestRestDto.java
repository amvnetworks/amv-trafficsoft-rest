package org.amv.trafficsoft.rest.asgregister.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "A resource representing the request for initializing a vehicle with OEM/series/model data.")
public class RegisterAsgRequestRestDto {
    @ApiModelProperty(notes = "Required. The vehicle KEY.", required = true)
    private String vehicleKey;

    @ApiModelProperty(notes = "Required. The oem CODE.", required = true)
    private String oemCode;

    @ApiModelProperty(notes = "Required. The series CODE.", required = true)
    private String seriesCode;

    @ApiModelProperty(notes = "Required. The model CODE.", required = true)
    private String modelCode;
}

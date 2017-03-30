package org.amv.trafficsoft.rest.asgregister.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "A resource representing a vehicle.")
public class VehicleRestDto {

    @ApiModelProperty(notes = "The ID of the vehicle.")
    private long id;

    @ApiModelProperty(notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(notes = "The series CODE.")
    private String seriesCode;

    @ApiModelProperty(notes = "The model CODE.")
    private String modelCode;
}

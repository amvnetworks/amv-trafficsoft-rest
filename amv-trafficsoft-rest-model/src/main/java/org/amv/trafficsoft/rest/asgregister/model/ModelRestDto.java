package org.amv.trafficsoft.rest.asgregister.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "A resource representing a vehicle model.")
public class ModelRestDto {
    @ApiModelProperty(notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(notes = "The series CODE.")
    private String seriesCode;

    @ApiModelProperty(notes = "The model CODE.")
    private String modelCode;

    @ApiModelProperty(notes = "A human readable representation of the model identifier.")
    private String name;
}

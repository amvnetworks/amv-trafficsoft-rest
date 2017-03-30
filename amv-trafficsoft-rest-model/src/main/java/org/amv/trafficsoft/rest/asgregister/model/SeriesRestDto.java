package org.amv.trafficsoft.rest.asgregister.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "A resource representing a vehicle series.")
public class SeriesRestDto {
    @ApiModelProperty(notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(notes = "The series CODE.")
    private String seriesCode;

    @ApiModelProperty(notes = "A human readable representation of the series identifier.")
    private String name;
}

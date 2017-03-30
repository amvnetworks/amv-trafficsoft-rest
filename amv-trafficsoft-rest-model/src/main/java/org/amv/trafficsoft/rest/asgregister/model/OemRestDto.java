package org.amv.trafficsoft.rest.asgregister.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "A resource representing a vehicle oem.")
public class OemRestDto {
    @ApiModelProperty(notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(notes = "A human readable representation of the oem identifier.")
    private String name;
}

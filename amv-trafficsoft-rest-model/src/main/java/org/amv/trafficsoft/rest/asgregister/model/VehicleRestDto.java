package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleRestDto.Builder.class)
@ApiModel(description = "A resource representing a vehicle.")
public class VehicleRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(name = "id", notes = "The ID of the vehicle.")
    private long id;

    @ApiModelProperty(name = "oemCode", notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(name = "seriesCode", notes = "The series CODE.")
    private String seriesCode;

    @ApiModelProperty(name = "modelCode", notes = "The model CODE.")
    private String modelCode;
}

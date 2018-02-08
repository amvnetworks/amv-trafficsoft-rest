package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ModelRestDto.Builder.class)
@ApiModel(description = "A resource representing a vehicle model.")
public class ModelRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(name = "oemCode", notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(name = "seriesCode", notes = "The series CODE.")
    private String seriesCode;

    @ApiModelProperty(name = "modelCode", notes = "The model CODE.")
    private String modelCode;

    @ApiModelProperty(name = "name", notes = "A human readable representation of the model identifier.")
    private String name;
}

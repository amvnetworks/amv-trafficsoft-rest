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

    @ApiModelProperty(notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(notes = "The series CODE.")
    private String seriesCode;

    @ApiModelProperty(notes = "The model CODE.")
    private String modelCode;

    @ApiModelProperty(notes = "A human readable representation of the model identifier.")
    private String name;
}

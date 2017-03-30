package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = SeriesRestDto.Builder.class)
@ApiModel(description = "A resource representing a vehicle series.")
public class SeriesRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(notes = "The oem CODE.")
    private String oemCode;

    @ApiModelProperty(notes = "The series CODE.")
    private String seriesCode;

    @ApiModelProperty(notes = "A human readable representation of the series identifier.")
    private String name;
}

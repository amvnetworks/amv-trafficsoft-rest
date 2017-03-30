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
@JsonDeserialize(builder = RegisterAsgRequestRestDto.Builder.class)
@ApiModel(description = "A resource representing the request for initializing a vehicle with OEM/series/model data.")
public class RegisterAsgRequestRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(notes = "Required. The vehicle KEY.", required = true)
    private String vehicleKey;

    @ApiModelProperty(notes = "Required. The oem CODE.", required = true)
    private String oemCode;

    @ApiModelProperty(notes = "Required. The series CODE.", required = true)
    private String seriesCode;

    @ApiModelProperty(notes = "Required. The model CODE.", required = true)
    private String modelCode;
}

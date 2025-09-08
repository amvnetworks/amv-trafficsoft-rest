package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = RegisterAsgRequestRestDto.Builder.class)
@Schema(description = "A resource representing the request for initializing a vehicle with OEM/series/model data.")
public class RegisterAsgRequestRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "vehicleKey", description = "Required. The vehicle KEY.", required = true)
    private String vehicleKey;

    @Schema(name = "oemCode", description = "Required. The oem CODE.", required = true)
    private String oemCode;

    @Schema(name = "seriesCode", description = "Required. The series CODE.", required = true)
    private String seriesCode;

    @Schema(name = "modelCode", description = "Required. The model CODE.", required = true)
    private String modelCode;
}

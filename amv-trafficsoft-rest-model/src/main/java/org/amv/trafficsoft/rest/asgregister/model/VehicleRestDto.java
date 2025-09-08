package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleRestDto.Builder.class)
@Schema(description = "A resource representing a vehicle.")
public class VehicleRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "id", description = "The ID of the vehicle.")
    private long id;

    @Schema(name = "oemCode", description = "The oem CODE.")
    private String oemCode;

    @Schema(name = "seriesCode", description = "The series CODE.")
    private String seriesCode;

    @Schema(name = "modelCode", description = "The model CODE.")
    private String modelCode;
}

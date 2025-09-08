package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = SeriesRestDto.Builder.class)
@Schema(description = "A resource representing a vehicle series.")
public class SeriesRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "oemCode", description = "The oem CODE.")
    private String oemCode;

    @Schema(name = "seriesCode", description = "The series CODE.")
    private String seriesCode;

    @Schema(name = "name", description = "A human readable representation of the series identifier.")
    private String name;
}

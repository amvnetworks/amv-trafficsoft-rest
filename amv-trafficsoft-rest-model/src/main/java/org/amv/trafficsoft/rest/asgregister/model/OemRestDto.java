package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = OemRestDto.Builder.class)
@Schema(description = "A resource representing a vehicle oem.")
public class OemRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "oemCode", description = "The oem CODE.")
    private String oemCode;

    @Schema(name = "name", description = "A human readable representation of the oem identifier.")
    private String name;
}

package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = RegisterAsgResponseRestDto.Builder.class)
@Schema(description = "A resource representing the response after initializing a vehicle with OEM/series/model data.")
public class RegisterAsgResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "vehicle", description = "A resource representing a vehicle.")
    private VehicleRestDto vehicle;
}
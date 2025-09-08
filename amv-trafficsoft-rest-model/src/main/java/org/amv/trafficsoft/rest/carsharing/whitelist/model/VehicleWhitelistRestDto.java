package org.amv.trafficsoft.rest.carsharing.whitelist.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = VehicleWhitelistRestDto.Builder.class)
public class VehicleWhitelistRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "vehicleId", description = "Id of a vehicle", required = true)
    private long vehicleId;

    @Schema(name = "whitelist", description = "A list of driver tag ids")
    @Singular(value = "addWhitelist")
    private List<String> whitelist;
}

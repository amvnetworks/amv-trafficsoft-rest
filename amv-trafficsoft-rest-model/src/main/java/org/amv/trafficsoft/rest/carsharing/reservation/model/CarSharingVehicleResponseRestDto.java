package org.amv.trafficsoft.rest.carsharing.reservation.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = CarSharingVehicleResponseRestDto.Builder.class)
public class CarSharingVehicleResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "vehicleId",
            description = "Required. The ID of the vehicle for which to get the reservations.",
            required = true)
    private long vehicleId;

    @Schema(name = "alwaysPowerOn",
            description = "Required. The 'alwaysPowerOn' configuration for the on-board unit. " +
                    "This can be necessary for car sharing vehicles to avoid dialing-in delays.",
            required = true)
    private boolean alwaysPowerOn;

    @Schema(name = "free", description = "Optional. True if the vehicle is available, otherwise false.")
    private boolean free;

    @Schema(name = "reservationIds", description = "Optional. The list of reservation IDs for this vehicle.")
    private List<Long> reservationIds;
}
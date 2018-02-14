package org.amv.trafficsoft.rest.carsharing.reservation.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
public class CarSharingVehicleResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(name = "vehicleId",
            notes = "Required. The ID of the vehicle for which to get the reservations.",
            required = true)
    private long vehicleId;

    @ApiModelProperty(name = "alwaysPowerOn",
            notes = "Required. The 'alwaysPowerOn' configuration for the on-board unit. " +
                    "This can be necessary for car sharing vehicles to avoid dialing-in delays.",
            required = true)
    private boolean alwaysPowerOn;

    @ApiModelProperty(name = "free", notes = "Optional. True if the vehicle is available, otherwise false.")
    private boolean free;

    @ApiModelProperty(name = "reservationIds", notes = "Optional. The list of reservation IDs for this vehicle.")
    private List<Long> reservationIds;
}
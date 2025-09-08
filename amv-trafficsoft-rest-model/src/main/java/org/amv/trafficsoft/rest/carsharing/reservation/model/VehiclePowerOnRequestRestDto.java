package org.amv.trafficsoft.rest.carsharing.reservation.model;


import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

@Getter
@Setter
public class VehiclePowerOnRequestRestDto {

    @Schema(name = "alwaysPowerOn", description = "Required. The 'alwaysPowerOn' configuration for the on-board unit. " +
            "This can be necessary for car sharing vehicles to avoid dialing-in delays.", required = true)
    private boolean alwaysPowerOn;

    public VehiclePowerOnRequestRestDto() {
        this(false);
    }

    public VehiclePowerOnRequestRestDto(boolean alwaysPowerOn) {
        this.alwaysPowerOn = alwaysPowerOn;
    }

}

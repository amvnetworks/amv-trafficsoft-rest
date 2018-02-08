package org.amv.trafficsoft.rest.carsharing.reservation.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiclePowerOnRequestRestDto {

    @ApiModelProperty(name = "alwaysPowerOn", notes = "Required. The 'alwaysPowerOn' configuration for the on-board unit. " +
            "This can be necessary for car sharing vehicles to avoid dialing-in delays.", required = true)
    private boolean alwaysPowerOn;

    public VehiclePowerOnRequestRestDto() {
        this(false);
    }

    public VehiclePowerOnRequestRestDto(boolean alwaysPowerOn) {
        this.alwaysPowerOn = alwaysPowerOn;
    }

}
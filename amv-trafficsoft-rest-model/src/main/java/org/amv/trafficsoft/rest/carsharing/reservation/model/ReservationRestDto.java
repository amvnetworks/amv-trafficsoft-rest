package org.amv.trafficsoft.rest.carsharing.reservation.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder(builderClassName = "Builder")
@ApiModel(description = "Data structure of a reservation. A ReservationRestDto can be of type 'RFID' or 'BTLE' (Bluetooth) " +
        "depending which detail meta data object is present: 'rfid' or 'btle'. One of both is mandatory so if both " +
        "objects are missing or both are present will result in an AMV TrafficSoft Error when trying to create or " +
        "modify a reservation. \nWhen using 'RFID' the properties valid from date/time and valid until date/time " +
        "are generally optional. So it's possible to create an infinite master reservation. For 'BTLE' or " +
        "limited reservations always both properties are mandatory."
)
public class ReservationRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(notes = "The vehicle ID.", required = true)
    private long vehicleId;

    @ApiModelProperty(notes = "The reservation ID. Leave empty for new reservations. " +
            "Required for reservation changes only.")
    private Long reservationId;

    @Deprecated
    @ApiModelProperty(notes = "DEPRECATED: The driver tag id (RFID). This is for backward compatibility only. For "
            + "new implementations use object 'rfid' instead!")
    private String driverTagId;

    @ApiModelProperty(notes = "The additional metadata for a RFID reservation. Required for reservations of type 'RFID'"
            + ". Leave empty for reservations of type 'BTLE' (Bluetooth).")
    private Rfid rfid;

    @ApiModelProperty(notes = "The additional metadata for a Bluetooth reservation. Required for reservations of type "
            + "'BTLE' (Bluetooth). Leave empty for reservations of type 'RFID'.")
    private Btle btle;

    @ApiModelProperty(notes = "The date and time the reservation is valid from. This property is generally optional but"
            + " required, when a valid until date/time is given.")
    private Date from;

    @ApiModelProperty(notes = "The date and time the reservation is valid until. This property is generally optional "
            + "but required, when a valid from date/time is given.")
    private Date until;

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @ApiModel(description = "The additional metadata for a reservation of type 'RFID'.")
    public static class Rfid {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @ApiModelProperty(notes = "Required. The driver tag id (RFID).", required = true)
        private String driverTagId;
    }

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @ApiModel(description = "The additional metadata for a reservation of type 'BTLE' (Bluetooth).")
    public static class Btle {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @ApiModelProperty(notes = "Required. The application id.", required = true)
        private String appId;

        @ApiModelProperty(notes = "Required. The mobile serial number (mosn).", required = true)
        private String mobileSerialNumber;
    }
}
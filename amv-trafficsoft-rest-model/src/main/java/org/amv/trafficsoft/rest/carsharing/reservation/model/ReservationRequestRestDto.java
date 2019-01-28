package org.amv.trafficsoft.rest.carsharing.reservation.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder(builderClassName = "Builder")
@ApiModel(description = "Data structure of a reservation. A reservation can be of type 'RFID' or 'BTLE' (Bluetooth) " +
        "depending which detail meta data object is present: 'rfid' or 'btle'. One of both is mandatory so both " +
        "objects missing or both present will result in an AMV TrafficSoft Error when trying to create or " +
        "modify a reservation. \nWhen using 'RFID' the properties valid from date/time and valid until date/time " +
        "are generally optional. So it's possible to create an infinite master reservation. For 'BTLE' or " +
        "limited reservations always both properties are mandatory.")
public class ReservationRequestRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(name = "vehicleId", notes = "The vehicle ID.", required = true)
    private long vehicleId;

    @ApiModelProperty(name = "reservationId", notes = "The reservation ID. Leave empty for new reservations. " +
            "Required for reservation changes only.")
    private Long reservationId;

    @Deprecated
    @ApiModelProperty(name = "driverTagId", notes = "DEPRECATED: The driver tag id (RFID). " +
            "This is for backward compatibility only. For new implementations use object 'rfid' instead!")
    private String driverTagId;

    @ApiModelProperty(name = "rfid",
            notes = "The additional metadata for a RFID reservation. Required for reservations of type 'RFID'"
            + ". Leave empty for reservations of type 'BTLE' (Bluetooth).")
    private Rfid rfid;

    @ApiModelProperty(name = "btle",
            notes = "The additional metadata for a Bluetooth reservation. Required for reservations of type " +
                    "'BTLE' (Bluetooth). Leave empty for reservations of type 'RFID'.")
    private Btle btle;

    @ApiModelProperty(name = "from",
            notes = "The date and time the reservation is valid from. This property is generally optional but" +
                    " required, when a valid until date/time is given.")
    private Date from;

    @ApiModelProperty(name = "until",
            notes = "The date and time the reservation is valid until. This property is generally optional " +
                    "but required, when a valid from date/time is given.")
    private Date until;

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @ApiModel(value = "RequestRfid", description = "The additional metadata for a reservation of type 'RFID'.")
    public static class Rfid {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @ApiModelProperty(name = "driverTagId", notes = "Required. The driver tag id (RFID).", required = true)
        private String driverTagId;
    }

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @ApiModel(value = "RequestBtle", description = "The additional metadata for a reservation of type 'BTLE' (Bluetooth).")
    public static class Btle {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @ApiModelProperty(name = "appId", notes = "Required. The application id.", required = true)
        private String appId;

        @ApiModelProperty(name = "version", notes = "Required. The certificate version.", required = true)
        private int version;

        @ApiModelProperty(name = "mobileSerialNumber",
                notes = "Required. The mobile serial number (mosn).",
                required = true)
        private String mobileSerialNumber;
    }
}
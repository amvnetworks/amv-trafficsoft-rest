package org.amv.trafficsoft.rest.carsharing.reservation.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder(builderClassName = "Builder")
public class ReservationResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(notes = "The vehicle id.", required = true)
    private long vehicleId;

    @ApiModelProperty(notes = "The reservation id.", required = true)
    private long reservationId;

    @Deprecated
    @ApiModelProperty(notes = "DEPRECATED: The driver tag id (RFID). This is for backward compatibility only.")
    private String driverTagId;

    @ApiModelProperty(notes = "The additional metadata for a RFID reservation. Only populated for 'RFID' reservations.")
    private Rfid rfid;

    @ApiModelProperty(notes = "The additional metadata for a Bluetooth reservation. Only populated for 'BTLE' reservations.")
    private Btle btle;

    @ApiModelProperty(notes = "The date and time the reservation is valid from.")
    private Date from;

    @ApiModelProperty(notes = "The date and time until the reservation is valid.")
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

        @ApiModelProperty(notes = "The application id.", required = true)
        private String appId;

        @ApiModelProperty(notes = "The mobile serial number (mosn).", required = true)
        private String mobileSerialNumber;

        @ApiModelProperty(notes = "The access certificate id.", required = true)
        private String accessCertificateId;
    }
}
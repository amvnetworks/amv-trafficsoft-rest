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

    @ApiModelProperty(name = "vehicleId", notes = "The vehicle id.", required = true)
    private long vehicleId;

    @ApiModelProperty(name = "reservationId", notes = "The reservation id.", required = true)
    private long reservationId;

    @Deprecated
    @ApiModelProperty(name = "driverTagId", notes = "DEPRECATED: The driver tag id (RFID). This is for backward compatibility only.")
    private String driverTagId;

    @ApiModelProperty(name = "rfid", notes = "The additional metadata for a RFID reservation. Only populated for 'RFID' reservations.")
    private Rfid rfid;

    @ApiModelProperty(name = "btle", notes = "The additional metadata for a Bluetooth reservation. Only populated for 'BTLE' reservations.")
    private Btle btle;

    @ApiModelProperty(name = "from", notes = "The date and time the reservation is valid from.")
    private Date from;

    @ApiModelProperty(name = "until", notes = "The date and time until the reservation is valid.")
    private Date until;

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @ApiModel(value = "ResponseRfid", description = "The additional metadata for a reservation of type 'RFID'.")
    public static class Rfid {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @ApiModelProperty(name = "driverTagId", notes = "Required. The driver tag id (RFID).", required = true)
        private String driverTagId;
    }

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @ApiModel(value = "ResponseBtle", description = "The additional metadata for a reservation of type 'BTLE' (Bluetooth).")
    public static class Btle {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @ApiModelProperty(name = "appId", notes = "The application id.", required = true)
        private String appId;

        @ApiModelProperty(name = "mobileSerialNumber", notes = "The mobile serial number (mosn).", required = true)
        private String mobileSerialNumber;

        @ApiModelProperty(name = "accessCertificateId", notes = "The access certificate id.", required = true)
        private String accessCertificateId;
    }
}
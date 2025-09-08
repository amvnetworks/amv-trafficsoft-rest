package org.amv.trafficsoft.rest.carsharing.reservation.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ReservationResponseRestDto.Builder.class)
public class ReservationResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "vehicleId", description = "The vehicle id.", required = true)
    private long vehicleId;

    @Schema(name = "reservationId", description = "The reservation id.", required = true)
    private long reservationId;

    @Deprecated
    @Schema(name = "driverTagId", description = "DEPRECATED: The driver tag id (RFID). This is for backward compatibility only.")
    private String driverTagId;

    @Schema(name = "rfid", description = "The additional metadata for a RFID reservation. Only populated for 'RFID' reservations.")
    private Rfid rfid;

    @Schema(name = "btle", description = "The additional metadata for a Bluetooth reservation. Only populated for 'BTLE' reservations.")
    private Btle btle;

    @Schema(name = "from", description = "The date and time the reservation is valid from.")
    private Date from;

    @Schema(name = "until", description = "The date and time until the reservation is valid.")
    private Date until;

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @JsonDeserialize(builder = ReservationResponseRestDto.Rfid.Builder.class)
    @Schema(name = "ResponseRfid", description = "The additional metadata for a reservation of type 'RFID'.")
    public static class Rfid {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @Schema(name = "driverTagId", description = "Required. The driver tag id (RFID).", required = true)
        private String driverTagId;
    }

    @Value
    @lombok.Builder(builderClassName = "Builder")
    @JsonDeserialize(builder = ReservationResponseRestDto.Btle.Builder.class)
    @Schema(name = "ResponseBtle", description = "The additional metadata for a reservation of type 'BTLE' (Bluetooth).")
    public static class Btle {
        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {

        }

        @Schema(name = "appId", description = "The application id.", required = true)
        private String appId;

        @Schema(name = "mobileSerialNumber", description = "The mobile serial number (mosn).", required = true)
        private String mobileSerialNumber;

        @Schema(name = "accessCertificateId", description = "The access certificate id.", required = true)
        private String accessCertificateId;
    }
}
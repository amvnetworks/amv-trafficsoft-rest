package org.amv.trafficsoft.rest.carsharing.reservation.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReservationResponseRestDtoMother {
    private ReservationResponseRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static ReservationResponseRestDto random() {
        return randomWithVehicleId(RandomUtils.nextLong());
    }

    public static ReservationResponseRestDto randomWithVehicleId(long vehicleId) {
        return randomBuilderWithVehicleId(vehicleId).build();
    }

    public static ReservationResponseRestDto.Builder randomBuilder() {
        return randomBuilderWithVehicleId(RandomUtils.nextLong());
    }

    public static ReservationResponseRestDto.Builder randomBuilderWithVehicleId(long vehicleId) {
        return ReservationResponseRestDto.builder()
                .reservationId(RandomUtils.nextLong())
                .vehicleId(vehicleId)
                .rfid(ReservationResponseRestDto.Rfid.builder()
                        .driverTagId(RandomStringUtils.randomNumeric(8))
                        .build())
                .btle(ReservationResponseRestDto.Btle.builder()
                        .mobileSerialNumber(RandomStringUtils.randomNumeric(8))
                        .appId(RandomStringUtils.randomAlphanumeric(12))
                        .accessCertificateId(UUID.randomUUID().toString())
                        .build())
                .from(Date.from(Instant.now()))
                .until(Date.from(Instant.now().plusSeconds(TimeUnit.DAYS.toSeconds(7))));
    }
}

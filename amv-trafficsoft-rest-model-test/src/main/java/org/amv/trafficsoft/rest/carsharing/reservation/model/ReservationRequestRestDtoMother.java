package org.amv.trafficsoft.rest.carsharing.reservation.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ReservationRequestRestDtoMother {
    private ReservationRequestRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static ReservationRequestRestDto random() {
        return randomWithVehicleId(RandomUtils.nextLong());
    }

    public static ReservationRequestRestDto randomWithVehicleId(long vehicleId) {
        return ReservationRequestRestDto.builder()
                .reservationId(RandomUtils.nextLong())
                .vehicleId(vehicleId)
                .rfid(ReservationRequestRestDto.Rfid.builder()
                        .driverTagId(RandomStringUtils.randomNumeric(8))
                        .build())
                .btle(ReservationRequestRestDto.Btle.builder()
                        .mobileSerialNumber(RandomStringUtils.randomNumeric(8))
                        .appId(RandomStringUtils.randomAlphanumeric(12))
                        .build())
                .from(Date.from(Instant.now()))
                .until(Date.from(Instant.now().plusSeconds(TimeUnit.DAYS.toSeconds(7))))
                .build();
    }
}

package org.amv.trafficsoft.rest.carsharing.reservation.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ReservationRestDtoMother {
    private ReservationRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static ReservationRestDto random() {
        return randomWithVehicleId(RandomUtils.nextLong());
    }

    public static ReservationRestDto randomWithVehicleId(long vehicleId) {
        return ReservationRestDto.builder()
                .reservationId(RandomUtils.nextLong())
                .vehicleId(vehicleId)
                .rfid(ReservationRestDto.Rfid.builder()
                        .driverTagId(RandomStringUtils.randomNumeric(8))
                        .build())
                .from(Date.from(Instant.now()))
                .until(Date.from(Instant.now().plusSeconds(TimeUnit.DAYS.toSeconds(7))))
                .build();
    }
}

package org.amv.trafficsoft.rest.carsharing.reservation.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class CarSharingVehicleRestDtoMother {
    private CarSharingVehicleRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static CarSharingVehicleRestDto random() {
        return randomWithVehicleId(RandomUtils.nextLong());
    }

    public static CarSharingVehicleRestDto randomWithVehicleId(long vehicleId) {
        return CarSharingVehicleRestDto.builder()
                .vehicleId(vehicleId)
                .free(RandomUtils.nextBoolean())
                .alwaysPowerOn(RandomUtils.nextBoolean())
                .reservationIds(IntStream.range(1, RandomUtils.nextInt(0, 9) + 1).boxed()
                        .map(foo -> RandomUtils.nextLong())
                        .collect(toList()))
                .build();
    }
}

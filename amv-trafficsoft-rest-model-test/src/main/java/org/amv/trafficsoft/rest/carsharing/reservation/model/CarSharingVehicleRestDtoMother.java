package org.amv.trafficsoft.rest.carsharing.reservation.model;

import org.apache.commons.lang3.RandomUtils;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class CarSharingVehicleRestDtoMother {
    private CarSharingVehicleRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static CarSharingVehicleResponseRestDto random() {
        return randomWithVehicleId(RandomUtils.nextLong());
    }

    public static CarSharingVehicleResponseRestDto randomWithVehicleId(long vehicleId) {
        return CarSharingVehicleResponseRestDto.builder()
                .vehicleId(vehicleId)
                .free(RandomUtils.nextBoolean())
                .alwaysPowerOn(RandomUtils.nextBoolean())
                .reservationIds(IntStream.range(1, RandomUtils.nextInt(0, 9) + 1).boxed()
                        .map(foo -> RandomUtils.nextLong())
                        .collect(toList()))
                .build();
    }
}

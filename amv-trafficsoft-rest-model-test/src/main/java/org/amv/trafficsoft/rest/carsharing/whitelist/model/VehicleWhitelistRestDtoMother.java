package org.amv.trafficsoft.rest.carsharing.whitelist.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public final class VehicleWhitelistRestDtoMother {
    private VehicleWhitelistRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static VehicleWhitelistRestDto random() {
        return randomForVehicleId(RandomUtils.nextLong());
    }

    public static VehicleWhitelistRestDto randomForVehicleId(long vehicleId) {
        return VehicleWhitelistRestDto.builder()
                .vehicleId(vehicleId)
                .whitelist(randomLongs().stream()
                        .map(i -> RandomStringUtils.randomAlphanumeric(8))
                        .collect(toList()))
                .build();
    }

    public static List<VehicleWhitelistRestDto> randomList() {
        return randomLongs().stream()
                .map(VehicleWhitelistRestDtoMother::randomForVehicleId)
                .collect(toList());
    }


    private static List<Long> randomLongs() {
        return IntStream.range(1, RandomUtils.nextInt(0, 9) + 2).boxed()
                .map(foo -> RandomUtils.nextLong())
                .collect(toList());
    }
}

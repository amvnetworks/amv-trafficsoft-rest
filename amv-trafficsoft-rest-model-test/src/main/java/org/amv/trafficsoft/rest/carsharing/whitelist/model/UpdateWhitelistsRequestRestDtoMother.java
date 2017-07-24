package org.amv.trafficsoft.rest.carsharing.whitelist.model;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public final class UpdateWhitelistsRequestRestDtoMother {
    private UpdateWhitelistsRequestRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static UpdateWhitelistsRequestRestDto random() {
        return randomForVehicleIds(randomLongs());
    }

    public static UpdateWhitelistsRequestRestDto randomForVehicleIds(List<Long> vehicleIds) {
        return UpdateWhitelistsRequestRestDto.builder()
                .vehicleWhitelists(vehicleIds.stream()
                        .map(VehicleWhitelistRestDtoMother::randomForVehicleId)
                        .collect(toList()))
                .build();
    }

    private static List<Long> randomLongs() {
        return IntStream.range(1, RandomUtils.nextInt(0, 9) + 2).boxed()
                .map(foo -> RandomUtils.nextLong())
                .collect(toList());
    }
}

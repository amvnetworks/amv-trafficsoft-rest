package org.amv.trafficsoft.rest.xfcd.model;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class DeliveryRestDtoMother {
    private DeliveryRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static DeliveryRestDto random() {
        return DeliveryRestDto.builder()
                .deliveryId(RandomUtils.nextLong())
                .timestamp(Date.from(Instant.now()))
                .track(TrackRestDtoMother.randomList())
                .build();
    }

    public static List<DeliveryRestDto> randomList() {
        return ImmutableList.<DeliveryRestDto>builder()
                .addAll(IntStream.range(1, RandomUtils.nextInt(5, 10))
                        .boxed()
                        .map(foo -> random())
                        .collect(Collectors.toList()))
                .build();
    }
}

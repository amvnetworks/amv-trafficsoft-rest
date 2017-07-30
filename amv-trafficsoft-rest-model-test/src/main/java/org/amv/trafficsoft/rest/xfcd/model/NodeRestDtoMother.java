package org.amv.trafficsoft.rest.xfcd.model;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class NodeRestDtoMother {
    private NodeRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static NodeRestDto random() {
        return NodeRestDto.builder()
                .id(RandomUtils.nextLong())
                .timestamp(Date.from(Instant.now()))
                .latitude(BigDecimal.valueOf(RandomUtils.nextDouble(0d, 180)))
                .longitude(BigDecimal.valueOf(RandomUtils.nextDouble(0d, 180)))
                .vdop(BigDecimal.valueOf(RandomUtils.nextDouble(0d, 100_000d)))
                .hdop(BigDecimal.valueOf(RandomUtils.nextDouble(0d, 100_000d)))
                .altitude(BigDecimal.valueOf(RandomUtils.nextDouble(0d, 100_000d)))
                .heading(BigDecimal.valueOf(RandomUtils.nextDouble(0d, 100_000d)))
                .speed(BigDecimal.valueOf(RandomUtils.nextDouble(0d, 100_000d)))
                .satellites(RandomUtils.nextInt())
                .xfcds(ParameterRestDtoMother.randomList())
                .states(ParameterRestDtoMother.randomList())
                .build();
    }

    public static List<NodeRestDto> randomList() {
        return ImmutableList.<NodeRestDto>builder()
                .addAll(IntStream.range(1, RandomUtils.nextInt(5, 10))
                        .boxed()
                        .map(foo -> random())
                        .collect(Collectors.toList()))
                .build();
    }
}

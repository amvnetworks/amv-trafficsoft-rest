package org.amv.trafficsoft.rest.xfcd.model;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ParameterRestDtoMother {
    private ParameterRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static ParameterRestDto random() {
        return ParameterRestDto.builder()
                .timestamp(Date.from(Instant.now()))
                .latitude(BigDecimal.valueOf(RandomUtils.nextDouble(0d, Math.pow(10, 4) - 1)))
                .longitude(BigDecimal.valueOf(RandomUtils.nextDouble(0d, Math.pow(10, 4) - 1)))
                .param(RandomStringUtils.randomAlphanumeric(10))
                .value(RandomStringUtils.randomAlphanumeric(10))
                .build();
    }

    public static List<ParameterRestDto> randomList() {
        return ImmutableList.<ParameterRestDto>builder()
                .addAll(IntStream.range(1, RandomUtils.nextInt(5, 10))
                        .boxed()
                        .map(foo -> random())
                        .collect(Collectors.toList()))
                .build();
    }
}

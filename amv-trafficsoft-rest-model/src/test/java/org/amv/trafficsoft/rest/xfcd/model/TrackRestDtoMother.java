package org.amv.trafficsoft.rest.xfcd.model;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TrackRestDtoMother {
    private TrackRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static TrackRestDto random() {
        return TrackRestDto.builder()
                .id(RandomUtils.nextLong())
                .nodes(NodeRestDtoMother.randomList())
                .build();
    }

    public static List<TrackRestDto> randomList() {
        return ImmutableList.<TrackRestDto>builder()
                .addAll(IntStream.range(1, RandomUtils.nextInt(5, 10))
                        .boxed()
                        .map(foo -> random())
                        .collect(Collectors.toList()))
                .build();
    }
}

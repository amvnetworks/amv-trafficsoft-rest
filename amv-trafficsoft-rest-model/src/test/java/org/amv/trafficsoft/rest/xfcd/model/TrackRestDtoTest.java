package org.amv.trafficsoft.rest.xfcd.model;

import com.google.common.collect.ImmutableCollection;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TrackRestDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        TrackRestDto trackDto = TrackRestDto.builder()
                .build();

        assertThat(trackDto, is(notNullValue()));
        assertThat(trackDto.getId(), is(0L));
        assertThat(trackDto.getVehicleId(), is(nullValue()));
        assertThat(trackDto.getNodes(), is(notNullValue()));
        assertThat(trackDto.getNodes(), hasSize(0));
    }

    @Test
    public void itShouldMakeUseOfGuavasImmutableClass() throws Exception {
        TrackRestDto trackDto = TrackRestDto.builder()
                .build();

        assertThat(trackDto.getNodes(), is(notNullValue()));
        assertThat(trackDto.getNodes(), is(instanceOf(ImmutableCollection.class)));
    }
}
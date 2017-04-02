package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TrackDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        TrackDto trackDto = TrackDto.builder()
                .build();

        assertThat(trackDto, is(notNullValue()));
        assertThat(trackDto.getId(), is(0L));
        assertThat(trackDto.getVehicleId(), is(nullValue()));
        assertThat(trackDto.getNodes(), is(notNullValue()));
        assertThat(trackDto.getNodes(), hasSize(0));
    }
}
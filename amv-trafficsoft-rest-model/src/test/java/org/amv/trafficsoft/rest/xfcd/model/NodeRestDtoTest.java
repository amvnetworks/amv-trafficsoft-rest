package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class NodeRestDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        NodeRestDto nodeDto = NodeRestDto.builder()
                .build();

        assertThat(nodeDto, is(notNullValue()));
        assertThat(nodeDto.getId(), is(0L));
        assertThat(nodeDto.getTimestamp(), is(nullValue()));
        assertThat(nodeDto.getAltitude(), is(nullValue()));
        assertThat(nodeDto.getHdop(), is(nullValue()));
        assertThat(nodeDto.getHeading(), is(nullValue()));
        assertThat(nodeDto.getLatitude(), is(nullValue()));
        assertThat(nodeDto.getLongitude(), is(nullValue()));
        assertThat(nodeDto.getSatellites(), is(nullValue()));
        assertThat(nodeDto.getSpeed(), is(nullValue()));
        assertThat(nodeDto.getVdop(), is(nullValue()));

        assertThat(nodeDto.getXfcds(), is(notNullValue()));
        assertThat(nodeDto.getXfcds(), hasSize(0));
        assertThat(nodeDto.getStates(), is(notNullValue()));
        assertThat(nodeDto.getStates(), hasSize(0));
    }

}
package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class NodeRestDtoMotherTest {
    @Test
    public void random() throws Exception {
        final NodeRestDto random = NodeRestDtoMother.random();

        assertThat(random, is(notNullValue()));
        assertThat(random.getId(), is(greaterThanOrEqualTo(0L)));
        assertThat(random.getLatitude(), is(notNullValue()));
        assertThat(random.getLongitude(), is(notNullValue()));
        assertThat(random.getSpeed(), is(notNullValue()));
        assertThat(random.getHdop(), is(notNullValue()));
        assertThat(random.getVdop(), is(notNullValue()));
        assertThat(random.getAltitude(), is(notNullValue()));
        assertThat(random.getHeading(), is(notNullValue()));
        assertThat(random.getSatellites(), is(greaterThanOrEqualTo(0)));
        assertThat(random.getStates(), is(notNullValue()));
        assertThat(random.getStates(), hasSize(greaterThan(0)));
        assertThat(random.getXfcds(), is(notNullValue()));
        assertThat(random.getXfcds(), hasSize(greaterThan(0)));
    }
}

package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TrackRestDtoMotherTest {
    @Test
    public void random() throws Exception {
        final TrackRestDto random = TrackRestDtoMother.random();

        assertThat(random, is(notNullValue()));
        assertThat(random.getId(), is(greaterThanOrEqualTo(0L)));
        assertThat(random.getVehicleId(), is(notNullValue()));
        assertThat(random.getNodes(), is(notNullValue()));
        assertThat(random.getNodes(), hasSize(greaterThan(0)));
    }

}

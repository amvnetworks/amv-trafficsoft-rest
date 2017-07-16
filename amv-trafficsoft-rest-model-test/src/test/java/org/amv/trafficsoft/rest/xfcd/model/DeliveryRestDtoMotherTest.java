package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DeliveryRestDtoMotherTest {
    @Test
    public void random() throws Exception {
        final DeliveryRestDto random = DeliveryRestDtoMother.random();

        assertThat(random, is(notNullValue()));
        assertThat(random.getDeliveryId(), is(greaterThanOrEqualTo(0L)));
        assertThat(random.getTimestamp(), is(notNullValue()));
        assertThat(random.getTrack(), is(notNullValue()));
        assertThat(random.getTrack(), hasSize(greaterThan(0)));
    }
}

package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ParameterRestDtoMotherTest {
    @Test
    public void random() throws Exception {
        final ParameterRestDto random = ParameterRestDtoMother.random();

        assertThat(random, is(notNullValue()));
        assertThat(random.getTimestamp(), is(notNullValue()));
        assertThat(random.getParam(), is(notNullValue()));
        assertThat(random.getValue(), is(notNullValue()));
        assertThat(random.getLatitude(), is(notNullValue()));
        assertThat(random.getLongitude(), is(notNullValue()));
    }

}

package org.amv.trafficsoft.rest.xfcd.model;

import com.google.common.collect.ImmutableCollection;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DeliveryRestDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        DeliveryRestDto deliveryDto = DeliveryRestDto.builder()
                .build();

        assertThat(deliveryDto, is(notNullValue()));
        assertThat(deliveryDto.getTimestamp(), is(nullValue()));
        assertThat(deliveryDto.getDeliveryId(), is(0L));
        assertThat(deliveryDto.getTrack(), is(notNullValue()));
        assertThat(deliveryDto.getTrack(), hasSize(0));
    }

    @Test
    public void itShouldMakeUseOfGuavasImmutableClass() throws Exception {
        DeliveryRestDto deliveryDto = DeliveryRestDto.builder()
                .build();

        assertThat(deliveryDto.getTrack(), is(notNullValue()));
        assertThat(deliveryDto.getTrack(), is(instanceOf(ImmutableCollection.class)));
    }
}
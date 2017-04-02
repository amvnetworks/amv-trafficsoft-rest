package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DeliveryDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .build();

        assertThat(deliveryDto, is(notNullValue()));
        assertThat(deliveryDto.getTimestamp(), is(nullValue()));
        assertThat(deliveryDto.getDeliveryId(), is(0L));
        assertThat(deliveryDto.getTrack(), is(notNullValue()));
        assertThat(deliveryDto.getTrack(), hasSize(0));
    }

}
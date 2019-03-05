package org.amv.trafficsoft.rest.carsharing.reservation.model;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ReservationRequestRestDtoTest {
    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        ReservationRequestRestDto dto = ReservationRequestRestDto.builder()
                .btle(ReservationRequestRestDto.Btle.builder()
                        .build())
                .build();

        assertThat(dto, is(notNullValue()));
        assertThat("default value of 'version' is defined", dto.getBtle().getVersion(), is(1));
    }

    @Test
    public void itShouldBePossibleToCreateBtleReservation() throws Exception {
        ReservationRequestRestDto dto = ReservationRequestRestDto.builder()
                .btle(ReservationRequestRestDto.Btle.builder()
                        .version(2)
                        .build())
                .build();

        assertThat(dto, is(notNullValue()));
        assertThat("default value of 'version' can be overwritten", dto.getBtle().getVersion(), is(2));
    }

}
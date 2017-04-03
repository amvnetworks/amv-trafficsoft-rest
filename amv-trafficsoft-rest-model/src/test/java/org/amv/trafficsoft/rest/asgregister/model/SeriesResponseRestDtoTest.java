package org.amv.trafficsoft.rest.asgregister.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class SeriesResponseRestDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        SeriesResponseRestDto modelsResponseDto = SeriesResponseRestDto.builder()
                .build();

        assertThat(modelsResponseDto, is(notNullValue()));
        assertThat(modelsResponseDto.getSeries(), is(notNullValue()));
        assertThat(modelsResponseDto.getSeries(), hasSize(0));
    }
}
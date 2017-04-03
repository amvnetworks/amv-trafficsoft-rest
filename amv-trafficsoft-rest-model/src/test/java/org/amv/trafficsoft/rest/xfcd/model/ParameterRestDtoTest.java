package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class ParameterRestDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        ParameterRestDto parameterDto = ParameterRestDto.builder()
                .build();

        assertThat(parameterDto, is(notNullValue()));
        assertThat(parameterDto.getTimestamp(), is(nullValue()));
        assertThat(parameterDto.getLatitude(), is(nullValue()));
        assertThat(parameterDto.getLongitude(), is(nullValue()));
        assertThat(parameterDto.getParam(), is(nullValue()));
        assertThat(parameterDto.getValue(), is(nullValue()));
    }
}
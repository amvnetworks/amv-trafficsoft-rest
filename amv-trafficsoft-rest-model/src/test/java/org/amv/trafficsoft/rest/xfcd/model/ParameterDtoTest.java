package org.amv.trafficsoft.rest.xfcd.model;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class ParameterDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        ParameterDto parameterDto = ParameterDto.builder()
                .build();

        assertThat(parameterDto, is(notNullValue()));
        assertThat(parameterDto.getTimestamp(), is(nullValue()));
        assertThat(parameterDto.getLatitude(), is(nullValue()));
        assertThat(parameterDto.getLongitude(), is(nullValue()));
        assertThat(parameterDto.getParam(), is(nullValue()));
        assertThat(parameterDto.getValue(), is(nullValue()));
    }
}
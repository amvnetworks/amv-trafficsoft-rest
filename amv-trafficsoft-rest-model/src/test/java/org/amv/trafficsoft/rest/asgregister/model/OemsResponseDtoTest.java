package org.amv.trafficsoft.rest.asgregister.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class OemsResponseDtoTest {

    @Test
    public void itShouldHaveEmptyListsAsDefault() throws Exception {
        OemsResponseDto modelsResponseDto = OemsResponseDto.builder()
                .build();

        assertThat(modelsResponseDto, is(notNullValue()));
        assertThat(modelsResponseDto.getOems(), is(notNullValue()));
        assertThat(modelsResponseDto.getOems(), hasSize(0));
    }
}
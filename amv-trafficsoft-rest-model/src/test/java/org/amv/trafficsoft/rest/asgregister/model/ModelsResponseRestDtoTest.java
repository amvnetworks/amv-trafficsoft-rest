package org.amv.trafficsoft.rest.asgregister.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class ModelsResponseRestDtoTest {

    @Test
    public void itShouldBePossibleToCreateObjectWithBuilder() throws Exception {
        ModelsResponseRestDto modelsResponseDto = ModelsResponseRestDto.builder()
                .build();

        assertThat(modelsResponseDto, is(notNullValue()));
        assertThat(modelsResponseDto.getModels(), is(notNullValue()));
        assertThat(modelsResponseDto.getModels(), hasSize(0));
    }

}
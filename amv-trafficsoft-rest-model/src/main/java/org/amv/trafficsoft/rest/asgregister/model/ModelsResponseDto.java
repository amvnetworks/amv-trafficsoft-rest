package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ModelsResponseDto.Builder.class)
@ApiModel(description = "A resource representing a container for vehicle models.")
public class ModelsResponseDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Singular("addModel")
    @ApiModelProperty(notes = "A list of vehicle models.")
    private List<ModelRestDto> models;

    public List<ModelRestDto> getModels() {
        return ImmutableList.copyOf(models);
    }

}

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
@JsonDeserialize(builder = OemsResponseDto.Builder.class)
@ApiModel(description = "A resource representing a container for vehicle oems.")
public class OemsResponseDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Singular("addOem")
    @ApiModelProperty(notes = "A list of vehicle oems.")
    private List<OemRestDto> oems;

    public List<OemRestDto> getOems() {
        return ImmutableList.copyOf(oems);
    }
}

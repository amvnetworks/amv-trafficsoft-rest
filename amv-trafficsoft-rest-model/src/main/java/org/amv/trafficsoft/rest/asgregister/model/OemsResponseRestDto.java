package org.amv.trafficsoft.rest.asgregister.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = OemsResponseRestDto.Builder.class)
@Schema(description = "A resource representing a container for vehicle oems.")
public class OemsResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Singular("addOem")
    @Schema(name = "oems", description = "A list of vehicle oems.")
    private List<OemRestDto> oems;

    public List<OemRestDto> getOems() {
        return ImmutableList.copyOf(oems);
    }
}

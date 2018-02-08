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
@JsonDeserialize(builder = SeriesResponseRestDto.Builder.class)
@ApiModel(description = "A resource representing a container for vehicle series.")
public class SeriesResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Singular("addSeries")
    @ApiModelProperty(name = "series", notes = "A list of vehicle series.")
    private List<SeriesRestDto> series;

    public List<SeriesRestDto> getSeries() {
        return ImmutableList.copyOf(series);
    }
}

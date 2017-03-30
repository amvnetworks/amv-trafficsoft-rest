package org.amv.trafficsoft.rest.asgregister.model;

import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(description = "A resource representing a container for vehicle series.")
public class SeriesResponseDto {
    @ApiModelProperty(notes = "A list of vehicle series.")
    private List<SeriesRestDto> series = Collections.emptyList();

    public List<SeriesRestDto> getSeries() {
        return ImmutableList.copyOf(series);
    }

    public void setSeries(List<SeriesRestDto> series) {
        this.series = ImmutableList.copyOf(series);
    }
}

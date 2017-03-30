package org.amv.trafficsoft.rest.asgregister.model;

import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(description = "A resource representing a container for vehicle oems.")
public class OemsResponseDto {
    @ApiModelProperty(notes = "A list of vehicle oems.")
    private List<OemRestDto> oems = Collections.emptyList();

    public List<OemRestDto> getOems() {
        return ImmutableList.copyOf(oems);
    }

    public void setOems(List<OemRestDto> oems) {
        this.oems = ImmutableList.copyOf(oems);
    }
}

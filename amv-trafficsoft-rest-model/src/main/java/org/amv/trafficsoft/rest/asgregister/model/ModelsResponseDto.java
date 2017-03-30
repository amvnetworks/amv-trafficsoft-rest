package org.amv.trafficsoft.rest.asgregister.model;

import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(description = "A resource representing a container for vehicle models.")
public class ModelsResponseDto {
    @ApiModelProperty(notes = "A list of vehicle models.")
    private List<ModelRestDto> models = Collections.emptyList();

    public List<ModelRestDto> getModels() {
        return ImmutableList.copyOf(models);
    }

    public void setModels(List<ModelRestDto> models) {
        this.models = ImmutableList.copyOf(models);
    }

}

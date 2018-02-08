package org.amv.trafficsoft.rest.xfcd.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

/**
 * One vehicle contains the vehicle ID = ASG ID and the corresponding data
 * nodes.
 */
@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = TrackRestDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The data for one vehicle.")
public class TrackRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(name = "id", notes = "Required. The ID of the track.", required = true)
    private long id;

    @ApiModelProperty(name = "vehicleId", notes = "Optional. The ID of the vehicle.")
    private Long vehicleId;

    @Singular("addNode")
    @ApiModelProperty(name = "nodes", notes = "Required. The actual data nodes of the vehicle.", required = true)
    private List<NodeRestDto> nodes;
}
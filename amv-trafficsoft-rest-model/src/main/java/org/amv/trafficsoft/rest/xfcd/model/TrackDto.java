
package org.amv.trafficsoft.rest.xfcd.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * One vehicle contains the vehicle ID = ASG ID and the corresponding data
 * nodes.
 *
 * @author <a href='mailto:elisabeth.rosemann@amv-networks.com'>Elisabeth
 *         Rosemann</a>
 * @version $Revision: 3582 $
 * @since 13.06.2016
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The data for one vehicle.")
public class TrackDto {
    @ApiModelProperty(notes = "Required. The ID of the track.", required = true)
    private long id;

    @ApiModelProperty(notes = "Optional. The ID of the vehicle.")
    private Long vehicleId;

    @ApiModelProperty(notes = "Required. The actual data nodes of the vehicle.", required = true)
    private List<NodeDto> nodes = Collections.emptyList();

    public List<NodeDto> getNodes() {
        return ImmutableList.copyOf(this.nodes);
    }

    public void setNodes(List<NodeDto> nodes) {
        this.nodes = ImmutableList.copyOf(nodes);
    }
}
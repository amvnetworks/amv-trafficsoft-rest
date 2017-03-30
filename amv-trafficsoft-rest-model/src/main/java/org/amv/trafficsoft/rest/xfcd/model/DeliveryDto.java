
package org.amv.trafficsoft.rest.xfcd.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A delivery contains multiple vehicles which can contain one or more data
 * points for the given vehicle.
 *
 * @author <a href='mailto:elisabeth.rosemann@amv-networks.com'>Elisabeth
 *         Rosemann</a>
 * @version $Revision: 3582 $
 * @since 13.06.2016
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "A delivery containing multiple XFCD data nodes for multiple vehicles.")
public class DeliveryDto {
    @ApiModelProperty(notes = "Required. The ID of the current delivery. Required for confirming the successful processing of the delivery.", required = true)
    private long deliveryId;

    @ApiModelProperty(notes = "Required. The timestamp when the delivery was created.", required = true)
    private Date timestamp;
    
    @ApiModelProperty(notes = "The list of currently active vehicles with a subscription to the given contract. Empty list if there are no active vehicles.")
    private List<TrackDto> track = Collections.emptyList();

    public Date getTimestamp() {
        return Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
    }

    public void setTimestamp(Date timestamp) {
        Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .ifPresent(date -> this.timestamp = date);
    }

    public List<TrackDto> getTrack() {
        return ImmutableList.copyOf(this.track);
    }

    public void setTrack(List<TrackDto> track) {
        this.track = ImmutableList.copyOf(track);
    }
}
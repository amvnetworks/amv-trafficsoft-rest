
package org.amv.trafficsoft.rest.xfcd.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A delivery contains multiple vehicles which can contain one or more data
 * points for the given vehicle.
 */
@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = DeliveryRestDto.Builder.class)
@ApiModel(description = "A delivery containing multiple XFCD data nodes for multiple vehicles.")
public class DeliveryRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(notes = "Required. The ID of the current delivery. Required for confirming the successful processing of the delivery.", required = true)
    private long deliveryId;

    @ApiModelProperty(notes = "Required. The timestamp when the delivery was created.", required = true)
    private Date timestamp;

    @Singular("addTrack")
    @ApiModelProperty(notes = "The list of currently active vehicles with a subscription to the given contract. Empty list if there are no active vehicles.")
    private List<TrackRestDto> track;

    public Date getTimestamp() {
        return Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
    }

    public List<TrackRestDto> getTrack() {
        return ImmutableList.copyOf(this.track);
    }
}
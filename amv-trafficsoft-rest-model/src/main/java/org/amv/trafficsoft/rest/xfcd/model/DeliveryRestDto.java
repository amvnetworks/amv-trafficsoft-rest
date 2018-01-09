package org.amv.trafficsoft.rest.xfcd.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A delivery contains multiple tracks which can contain one or more data
 * points from multiple vehicles.
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
    @ApiModelProperty(notes = "The list of data sent by currently active vehicles with a subscription to the given contract. " +
            "Empty list if there is no data from active vehicles at the moment.")
    private List<TrackRestDto> track;

    public Date getTimestamp() {
        return Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
    }
}
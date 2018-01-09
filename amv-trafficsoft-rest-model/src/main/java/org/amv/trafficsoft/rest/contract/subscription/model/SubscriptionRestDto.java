package org.amv.trafficsoft.rest.contract.subscription.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = SubscriptionRestDto.Builder.class)
public class SubscriptionRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    private long vehicleId;
    private Date from;
    private Date until;
}

package org.amv.trafficsoft.rest.contract.subscription.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = SubscriptionsResponseRestDto.Builder.class)
public class SubscriptionsResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Singular("addSubscription")
    private List<SubscriptionRestDto> subscriptions;
}

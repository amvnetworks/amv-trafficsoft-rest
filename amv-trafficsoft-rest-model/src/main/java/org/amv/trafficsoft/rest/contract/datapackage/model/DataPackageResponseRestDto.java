package org.amv.trafficsoft.rest.contract.datapackage.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = DataPackageResponseRestDto.Builder.class)
public class DataPackageResponseRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    private boolean gpsClean;

    private ParamsRestDto params;
}

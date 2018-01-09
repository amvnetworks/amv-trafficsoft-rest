package org.amv.trafficsoft.rest.contract.datapackage.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = DopuxParamRestDto.Builder.class)
public class DopuxParamRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    private String code;
    private String name;
    private String description;
}

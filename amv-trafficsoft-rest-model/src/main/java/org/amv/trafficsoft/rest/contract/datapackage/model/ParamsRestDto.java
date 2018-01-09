package org.amv.trafficsoft.rest.contract.datapackage.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ParamsRestDto.Builder.class)
public class ParamsRestDto {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Singular("addDopuxParam")
    private List<DopuxParamRestDto> dopux;

    @Singular("addXfcdParam")
    private List<XfcdParamRestDto> xfcds;

    @Singular("addStateParam")
    private List<StateParamRestDto> states;
}

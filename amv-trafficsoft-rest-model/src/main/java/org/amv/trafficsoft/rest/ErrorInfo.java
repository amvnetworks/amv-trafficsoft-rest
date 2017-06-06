package org.amv.trafficsoft.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * {
 * "dateTime":"2042-01-10T14:15:33",
 * "exception":"org.amv.trafficsoft.web.TSWebException",
 * "errorCode":"TSW-00001",
 * "id":"6POYG",
 * "message":"[TSW-00001] Some message here!",
 * "url":"http://www.example.com"
 * }
 */
@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ErrorInfo.Builder.class)
@ApiModel(description = "Information about an occurred exception")
public class ErrorInfo {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    private String id;
    private LocalDateTime dateTime;
    private String errorCode;
    private String exception;
    private String message;
    private String url;
}

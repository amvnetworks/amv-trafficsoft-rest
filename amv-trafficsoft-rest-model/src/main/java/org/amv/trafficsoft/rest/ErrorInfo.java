package org.amv.trafficsoft.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * A resource representing information about a TrafficSoft error.
 *
 * <p>
 * For example:
 * <pre>{@code
 * {
 *   "dateTime":"2042-01-10T14:15:33",
 *   "exception":"org.amv.trafficsoft.web.TSWebException",
 *   "errorCode":"TSW-00001",
 *   "id":"6POYG",
 *   "message":"[TSW-00001] Some message here!",
 *   "url":"http://www.example.com"
 * }
 * }</pre>
 */
@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ErrorInfo.Builder.class)
@ApiModel(description = "A resource representing information about an error")
public class ErrorInfo {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @ApiModelProperty(name = "id", notes = "An id uniquely identifying the error")
    private String id;

    @ApiModelProperty(name = "dateTime", notes = "The time the error occurred")
    private LocalDateTime dateTime;

    @ApiModelProperty(name = "errorCode", notes = "An error code")
    private String errorCode;

    @ApiModelProperty(name = "exception", notes = "Name of the exception that caused the error")
    private String exception;

    @ApiModelProperty(name = "message", notes = "An additional message explaining the error")
    private String message;

    @ApiModelProperty(name = "url", notes = "The source the error originally occurred")
    private String url;
}

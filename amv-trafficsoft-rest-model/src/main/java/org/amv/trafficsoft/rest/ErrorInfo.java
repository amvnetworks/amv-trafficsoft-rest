package org.amv.trafficsoft.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * A resource representing information about a Trafficsoft error.
 *
 * <p> For example:
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

    /**
     * @return An id uniquely identifying the error
     */
    @ApiModelProperty(notes = "An id uniquely identifying the error")
    private String id;

    /**
     * @return The time the error occurred
     */
    @ApiModelProperty(notes = "The time the error occurred")
    private LocalDateTime dateTime;

    /**
     * @return An error code
     */
    @ApiModelProperty(notes = "An error code")
    private String errorCode;

    /**
     * @return Name of the exception that caused the error
     */
    @ApiModelProperty(notes = "Name of the exception that caused the error")
    private String exception;

    /**
     * @return An additional message explaining the error
     */
    @ApiModelProperty(notes = "An additional message explaining the error")
    private String message;

    /**
     * @return The source the error originally occurred
     */
    @ApiModelProperty(notes = "The source the error originally occurred")
    private String url;
}

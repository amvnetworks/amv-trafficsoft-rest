package org.amv.trafficsoft.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "A resource representing information about an error")
public class ErrorInfo {
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }

    @Schema(name = "id", description = "An id uniquely identifying the error")
    private String id;

    @Schema(name = "dateTime", description = "The time the error occurred")
    private LocalDateTime dateTime;

    @Schema(name = "errorCode", description = "An error code")
    private String errorCode;

    @Schema(name = "exception", description = "Name of the exception that caused the error")
    private String exception;

    @Schema(name = "message", description = "An additional message explaining the error")
    private String message;

    @Schema(name = "url", description = "The source the error originally occurred")
    private String url;
}

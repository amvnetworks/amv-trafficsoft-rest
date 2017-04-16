package org.amv.trafficsoft.rest.client;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.amv.trafficsoft.rest.ErrorInfo;

import static java.util.Objects.requireNonNull;

@Value
@EqualsAndHashCode(callSuper = true)
public class TrafficsoftException extends Exception {
    private final ErrorInfo errorInfo;

    TrafficsoftException(ErrorInfo errorInfo, Throwable cause) {
        super(cause);
        this.errorInfo = requireNonNull(errorInfo);
    }
}

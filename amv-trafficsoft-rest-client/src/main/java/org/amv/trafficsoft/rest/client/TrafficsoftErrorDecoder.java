package org.amv.trafficsoft.rest.client;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.amv.trafficsoft.rest.ErrorInfo;

import java.util.Optional;

import static feign.FeignException.errorStatus;
import static java.util.Objects.requireNonNull;

class TrafficsoftErrorDecoder implements ErrorDecoder {

    private final Decoder jsonDecoder;

    TrafficsoftErrorDecoder(Decoder jsonDecoder) {
        this.jsonDecoder = requireNonNull(jsonDecoder);
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException feignException = errorStatus(methodKey, response);

        return parseErrorInfo(response)
                .map(errorInfo -> (Exception) new TrafficsoftException(errorInfo, feignException))
                .orElse(feignException);
    }

    private Optional<ErrorInfo> parseErrorInfo(Response response) {
        try {
            ErrorInfo errorInfo = (ErrorInfo) this.jsonDecoder.decode(response, ErrorInfo.class);
            return Optional.ofNullable(errorInfo)
                    .filter(info -> info.getId() != null);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

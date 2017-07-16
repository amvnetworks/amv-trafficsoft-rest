package org.amv.trafficsoft.rest;

import org.amv.trafficsoft.rest.ErrorInfo;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

public final class ErrorInfoRestDtoMother {
    private ErrorInfoRestDtoMother() {
        throw new UnsupportedOperationException();
    }

    public static ErrorInfo random() {
        return ErrorInfo.builder()
                .id(RandomStringUtils.randomAlphanumeric(6))
                .dateTime(LocalDateTime.now())
                .errorCode(RandomStringUtils.randomNumeric(6))
                .exception(RandomStringUtils.randomAlphanumeric(10))
                .message(RandomStringUtils.randomAlphanumeric(10))
                .url(RandomStringUtils.randomAlphanumeric(10))
                .build();
    }
}

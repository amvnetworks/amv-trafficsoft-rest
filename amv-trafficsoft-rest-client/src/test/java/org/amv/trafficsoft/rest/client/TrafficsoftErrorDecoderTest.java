package org.amv.trafficsoft.rest.client;

import com.google.common.base.Charsets;
import feign.FeignException;
import feign.Response;
import feign.jackson.JacksonDecoder;
import org.amv.trafficsoft.rest.ErrorInfo;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.amv.trafficsoft.rest.client.ClientConfig.ConfigurableClientConfig.defaultObjectMapper;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TrafficsoftErrorDecoderTest {

    private TrafficsoftErrorDecoder sut;

    @Before
    public void setUp() {
        this.sut = new TrafficsoftErrorDecoder(new JacksonDecoder(defaultObjectMapper));
    }

    @Test
    public void itShouldWrapTrafficsoftErrors() throws Exception {
        String json = "{" +
                "\"dateTime\":\"2042-01-10T14:15:33\"," +
                "\"exception\":\"org.amv.trafficsoft.web.TSWebException\"," +
                "\"errorCode\":\"TSW-00001\"," +
                "\"id\":\"6POYG\"," +
                "\"message\":\"[TSW-00001] Some message here!\"," +
                "\"url\":\"https://www.example.com\"" +
                "}";
        Response response = Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .reason("ANY_REASON")
                .body(json, Charsets.UTF_8)
                .headers(Collections.emptyMap())
                .build();

        String anyMethodKey = RandomStringUtils.randomAlphanumeric(10);
        final Exception exception = sut.decode(anyMethodKey, response);

        assertThat(exception, is(notNullValue()));
        assertThat(exception, instanceOf(TrafficsoftException.class));

        TrafficsoftException tsException = (TrafficsoftException) exception;
        final ErrorInfo errorInfo = tsException.getErrorInfo();

        assertThat(errorInfo, is(notNullValue()));
        assertThat(errorInfo.getId(), is("6POYG"));
        assertThat(errorInfo.getErrorCode(), is("TSW-00001"));
        assertThat(errorInfo.getException(), is("org.amv.trafficsoft.web.TSWebException"));
        assertThat(errorInfo.getMessage(), is("[TSW-00001] Some message here!"));
    }

    @Test
    public void itShouldNotWrapNonTrafficsoftErrors() throws Exception {
        String json = "{" +
                "\"dateTime\":\"2042-01-10T14:15:33\"," +
                "\"exception\":\"java.lang.IllegalArgumentException\"," +
                "\"message\":\"\"" +
                "}";
        Response response = Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .reason("ANY_REASON")
                .body(json, Charsets.UTF_8)
                .headers(Collections.emptyMap())
                .build();

        String anyMethodKey = RandomStringUtils.randomAlphanumeric(10);
        final Exception exception = sut.decode(anyMethodKey, response);

        assertThat(exception, is(notNullValue()));
        assertThat(exception, instanceOf(FeignException.class));
    }

}
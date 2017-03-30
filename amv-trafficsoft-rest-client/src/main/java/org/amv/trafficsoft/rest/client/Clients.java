package org.amv.trafficsoft.rest.client;

import com.google.common.collect.ImmutableList;
import feign.RequestInterceptor;
import feign.hystrix.HystrixFeign;

import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class Clients {

    private Clients() {
        throw new UnsupportedOperationException();
    }

    public static <T> T create(ClientConfig<T> clientConfig) {
        requireNonNull(clientConfig, "`clientConfig` must not be null");

        final ImmutableList<RequestInterceptor> requestInterceptors =
                ImmutableList.<RequestInterceptor>builder()
                        .addAll(firstNonNull(clientConfig.requestInterceptors(), emptyList()))
                        .addAll(Stream.of(clientConfig.basicAuthRequestInterceptor())
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(toList()))
                        .build();

        return HystrixFeign.builder()
                .client(clientConfig.client())
                .contract(clientConfig.contract())
                .decoder(clientConfig.decoder())
                .encoder(clientConfig.encoder())
                .errorDecoder(clientConfig.errorDecoder())
                .logger(clientConfig.logger())
                .logLevel(clientConfig.logLevel())
                .options(clientConfig.options())
                .requestInterceptors(requestInterceptors)
                .retryer(clientConfig.retryer())
                .setterFactory(clientConfig.setterFactory())
                .target(clientConfig.target());
    }
}
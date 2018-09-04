package org.amv.trafficsoft.rest.client.xfcd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import feign.Response;
import feign.Target;
import feign.hystrix.FallbackFactory;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.ErrorInfoRestDtoMother;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.xfcd.model.DeliveryRestDto;
import org.amv.trafficsoft.rest.xfcd.model.NodeRestDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class XfcdClientWithFallbackTest {
    private static final long NON_EXISTING_CONTRACT_ID = -1L;
    private XfcdClient sut;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper jsonMapper = ClientConfig.ConfigurableClientConfig.defaultObjectMapper;

        String exceptionJson = jsonMapper.writeValueAsString(ErrorInfoRestDtoMother.random());

        MockClient mockClient = new MockClient()
                .add(HttpMethod.GET, String.format("/api/rest/v1/xfcd?contractId=%d", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body(exceptionJson, Charsets.UTF_8));

        Target<XfcdClient> mockTarget = new MockTarget<>(XfcdClient.class);

        FallbackFactory<XfcdClient> xfcdClientFallbackFactory = new SimpleXfcdClientFallbackFactory();

        ClientConfig<XfcdClient> config = ClientConfig.ConfigurableClientConfig.<XfcdClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .fallbackFactory(xfcdClientFallbackFactory)
                .build();

        this.sut = TrafficsoftClients.xfcd(config);

    }

    @Test
    public void itShouldReturnFallbackOnException() {
        HystrixCommand<List<DeliveryRestDto>> data = sut.getData(NON_EXISTING_CONTRACT_ID);
        List<DeliveryRestDto> returnValue = data.execute();

        assertThat(returnValue, is(notNullValue()));
        assertThat(returnValue, hasSize(0));
    }

    class SimpleXfcdClientFallbackFactory implements FallbackFactory<XfcdClient> {
        @Override
        public XfcdClient create(Throwable cause) {
            HystrixCommandGroupKey hystrixCommandGroupKey = HystrixCommandGroupKey.Factory.asKey("");
            return new XfcdClient() {
                @Override
                public HystrixCommand<List<DeliveryRestDto>> getDataAndConfirmDeliveries(long contractId, List<Long> deliveryIds) {
                    return new HystrixCommand<List<DeliveryRestDto>>(hystrixCommandGroupKey) {
                        @Override
                        protected List<DeliveryRestDto> run() {
                            return Lists.newArrayList();
                        }
                    };
                }

                @Override
                public HystrixCommand<List<DeliveryRestDto>> getData(long contractId) {
                    return new HystrixCommand<List<DeliveryRestDto>>(hystrixCommandGroupKey) {
                        @Override
                        protected List<DeliveryRestDto> run() {
                            return Lists.newArrayList();
                        }
                    };
                }

                @Override
                public HystrixCommand<Void> confirmDeliveries(long contractId, List<Long> deliveryIds) {
                    return new HystrixCommand<Void>(hystrixCommandGroupKey) {
                        @Override
                        protected Void run() {
                            return null;
                        }
                    };
                }

                @Override
                public HystrixCommand<List<NodeRestDto>> getLatestData(long contractId, List<Long> vehicleIds) {
                    return new HystrixCommand<List<NodeRestDto>>(hystrixCommandGroupKey) {
                        @Override
                        protected List<NodeRestDto> run() {
                            return Lists.newArrayList();
                        }
                    };
                }
            };
        }
    }
}

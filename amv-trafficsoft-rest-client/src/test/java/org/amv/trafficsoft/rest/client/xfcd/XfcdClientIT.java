package org.amv.trafficsoft.rest.client.xfcd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import feign.Response;
import feign.Target;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.xfcd.model.DeliveryDto;
import org.amv.trafficsoft.rest.xfcd.model.NodeDto;
import org.amv.trafficsoft.rest.xfcd.model.ParameterDto;
import org.amv.trafficsoft.rest.xfcd.model.TrackDto;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XfcdClientIT {
    private static final long NON_EXISTING_CONTRACT_ID = -1L;
    private static final long ANY_CONTRACT_ID = RandomUtils.nextLong();
    private static final List<Long> VALID_VEHICLE_IDS = ImmutableList.of(RandomUtils.nextLong(), RandomUtils.nextLong());


    private TestScheduler testScheduler = Schedulers.test();

    private XfcdClient sut;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper jsonMapper = new ObjectMapper();

        ParameterDto parameterDto = ParameterDto.builder()
                .latitude(BigDecimal.ONE)
                .longitude(BigDecimal.ONE)
                .timestamp(Date.valueOf(LocalDate.now()))
                .param("anyParam")
                .value("anyValue")
                .build();
        NodeDto nodeDto = NodeDto.builder()
                .addXfcd(parameterDto)
                .build();
        TrackDto trackDto = TrackDto.builder()
                .addNode(nodeDto)
                .build();
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .addTrack(trackDto)
                .build();

        String deliveryDtoListAsJson = jsonMapper.writeValueAsString(Lists.newArrayList(deliveryDto));
        String nodeDtoListAsJson = jsonMapper.writeValueAsString(Lists.newArrayList(deliveryDto));

        MockClient mockClient = new MockClient()
                .add(HttpMethod.GET, String.format("/%d/xfcd", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body("{}", Charsets.UTF_8)
                        .build())
                .ok(HttpMethod.GET, String.format("/%d/xfcd", ANY_CONTRACT_ID), deliveryDtoListAsJson)
                .ok(HttpMethod.POST, String.format("/%d/xfcd/last", ANY_CONTRACT_ID), nodeDtoListAsJson);

        Target<XfcdClient> mockTarget = new MockTarget<>(XfcdClient.class);

        ClientConfig<XfcdClient> config = ClientConfig.ConfigurableClientConfig.<XfcdClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        this.sut = TrafficsoftClients.xfcd(config);
    }

    @Test
    public void itShouldThrowExceptionOnMismatchingContractId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean onErrorCalled = new AtomicBoolean(false);
        sut.getData(NON_EXISTING_CONTRACT_ID)
                .toObservable()
                .subscribeOn(testScheduler)
                .subscribe(new TestSubscriber<List<DeliveryDto>>() {
                    @Override
                    public void onError(Throwable e) {
                        assertThat(e, instanceOf(HystrixRuntimeException.class));
                        assertThat(e.getCause(), instanceOf(FeignException.class));
                        onErrorCalled.set(true);
                        latch.countDown();
                    }

                    @Override
                    public void onNext(List<DeliveryDto> deliveryDtos) {
                        Assert.fail("Should have thrown exception and called onError");
                        latch.countDown();
                    }
                });

        testScheduler.triggerActions();

        latch.await();

        assertThat(onErrorCalled.get(), is(true));
    }

    @Test
    public void itShouldGetDataAndConfirmDelivieries() {
        // TODO: implement me
    }

    @Test
    public void itShouldConfirmDelivieries() {
        // TODO: implement me
    }

    @Test
    public void itShouldGetData() {
        List<DeliveryDto> deliveries = sut.getData(ANY_CONTRACT_ID).execute();

        assertThat(deliveries, is(notNullValue()));
        assertThat(deliveries, hasSize(greaterThan(0)));

        DeliveryDto anyDelivery = deliveries.stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyDelivery, is(notNullValue()));
        assertThat(anyDelivery.getTrack(), is(notNullValue()));
        assertThat(anyDelivery.getTrack(), hasSize(greaterThan(0)));

        TrackDto anyTrack = anyDelivery.getTrack().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyTrack, is(notNullValue()));
        assertThat(anyTrack.getNodes(), is(notNullValue()));
        assertThat(anyTrack.getNodes(), hasSize(greaterThan(0)));

        NodeDto anyNode = anyTrack.getNodes().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyNode, is(notNullValue()));
    }

    @Test
    public void itShouldGetLastData() {
        List<NodeDto> nodes = sut.getLastData(ANY_CONTRACT_ID, VALID_VEHICLE_IDS).execute();

        assertThat(nodes, is(notNullValue()));
    }
}

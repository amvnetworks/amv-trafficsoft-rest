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
import org.amv.trafficsoft.rest.ErrorInfo;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.client.TrafficsoftException;
import org.amv.trafficsoft.rest.xfcd.model.DeliveryRestDto;
import org.amv.trafficsoft.rest.xfcd.model.NodeRestDto;
import org.amv.trafficsoft.rest.xfcd.model.ParameterRestDto;
import org.amv.trafficsoft.rest.xfcd.model.TrackRestDto;
import org.apache.commons.lang.RandomStringUtils;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
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
        ObjectMapper jsonMapper = ClientConfig.ConfigurableClientConfig.defaultObjectMapper;

        ParameterRestDto xfcdParameterDto = ParameterRestDto.builder()
                .timestamp(Date.from(Instant.now()))
                .latitude(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .longitude(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .timestamp(Date.valueOf(LocalDate.now()))
                .param(RandomStringUtils.randomAlphanumeric(10))
                .value(RandomStringUtils.randomAlphanumeric(10))
                .build();

        ParameterRestDto stateParameterDto = ParameterRestDto.builder()
                .timestamp(Date.from(Instant.now()))
                .latitude(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .longitude(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .timestamp(Date.valueOf(LocalDate.now()))
                .param(RandomStringUtils.randomAlphanumeric(10))
                .value(RandomStringUtils.randomAlphanumeric(10))
                .build();

        NodeRestDto nodeDto = NodeRestDto.builder()
                .id(RandomUtils.nextLong())
                .timestamp(Date.from(Instant.now()))
                .latitude(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .longitude(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .vdop(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .hdop(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .altitude(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .heading(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .speed(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .satellites(RandomUtils.nextInt())
                .addXfcd(xfcdParameterDto)
                .addState(stateParameterDto)
                .build();

        TrackRestDto trackDto = TrackRestDto.builder()
                .id(RandomUtils.nextLong())
                .addNode(nodeDto)
                .build();

        DeliveryRestDto deliveryDto = DeliveryRestDto.builder()
                .deliveryId(RandomUtils.nextLong())
                .timestamp(Date.from(Instant.now()))
                .addTrack(trackDto)
                .build();

        String deliveryDtoListAsJson = jsonMapper.writeValueAsString(Lists.newArrayList(deliveryDto));
        String nodeDtoListAsJson = jsonMapper.writeValueAsString(Lists.newArrayList(nodeDto));

        String exceptionJson = jsonMapper.writeValueAsString(ErrorInfo.builder()
                .id(RandomStringUtils.randomAlphanumeric(6))
                .dateTime(LocalDateTime.now())
                .errorCode(RandomStringUtils.randomNumeric(6))
                .exception(RandomStringUtils.randomAlphanumeric(10))
                .url(RandomStringUtils.randomAlphanumeric(10))
                .build());

        MockClient mockClient = new MockClient()
                .add(HttpMethod.GET, String.format("/%d/xfcd", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body(exceptionJson, Charsets.UTF_8))
                .add(HttpMethod.POST, String.format("/%d/xfcd/confirm", ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .headers(Collections.emptyMap()))
                .ok(HttpMethod.POST, String.format("/%d/xfcd", ANY_CONTRACT_ID), deliveryDtoListAsJson)
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
                .subscribe(new TestSubscriber<List<DeliveryRestDto>>() {
                    @Override
                    public void onError(Throwable e) {
                        assertThat(e, instanceOf(HystrixRuntimeException.class));
                        assertThat(e.getCause(), instanceOf(TrafficsoftException.class));
                        assertThat(e.getCause().getCause(), instanceOf(FeignException.class));
                        onErrorCalled.set(true);

                        latch.countDown();
                    }

                    @Override
                    public void onNext(List<DeliveryRestDto> deliveryDtos) {
                        latch.countDown();

                        Assert.fail("Should have thrown exception and called onError");
                    }
                });

        testScheduler.triggerActions();

        latch.await(10, TimeUnit.SECONDS);

        assertThat(onErrorCalled.get(), is(true));
    }

    @Test
    public void itShouldGetDataAndConfirmDeliveries() {
        List<DeliveryRestDto> deliveries = sut.getDataAndConfirmDeliveries(ANY_CONTRACT_ID, LongStream.range(1L, 10L)
                .boxed()
                .collect(toList()))
                .execute();

        assertThat(deliveries, is(notNullValue()));
        assertThat(deliveries, hasSize(greaterThan(0)));

        DeliveryRestDto firstDelivery = deliveries.stream().findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(firstDelivery, is(notNullValue()));
        assertThat(firstDelivery.getDeliveryId(), is(greaterThanOrEqualTo(0L)));
        assertThat(firstDelivery.getTimestamp(), is(notNullValue()));
        assertThat(firstDelivery.getTrack(), is(notNullValue()));
        assertThat(firstDelivery.getTrack(), hasSize(greaterThan(0)));

        TrackRestDto anyTrack = firstDelivery.getTrack().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyTrack, is(notNullValue()));
        assertThat(anyTrack.getNodes(), is(notNullValue()));
        assertThat(anyTrack.getNodes(), hasSize(greaterThan(0)));

        NodeRestDto firstNode = anyTrack.getNodes().stream().findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(firstNode, is(notNullValue()));
        assertThat(firstNode.getId(), is(greaterThanOrEqualTo(0L)));
        assertThat(firstNode.getLatitude(), is(notNullValue()));
        assertThat(firstNode.getLongitude(), is(notNullValue()));
        assertThat(firstNode.getSpeed(), is(notNullValue()));
        assertThat(firstNode.getHdop(), is(notNullValue()));
        assertThat(firstNode.getVdop(), is(notNullValue()));
        assertThat(firstNode.getAltitude(), is(notNullValue()));
        assertThat(firstNode.getHeading(), is(notNullValue()));
        assertThat(firstNode.getSatellites(), is(greaterThanOrEqualTo(0)));
        assertThat(firstNode.getStates(), is(notNullValue()));
        assertThat(firstNode.getStates(), hasSize(greaterThan(0)));
        assertThat(firstNode.getXfcds(), is(notNullValue()));
        assertThat(firstNode.getXfcds(), hasSize(greaterThan(0)));
    }

    @Test
    public void itShouldConfirmDeliveries() {
        Void returnValue = sut.confirmDeliveries(ANY_CONTRACT_ID, LongStream.range(1L, 10L)
                .boxed()
                .collect(toList()))
                .execute();

        assertThat(returnValue, is(nullValue()));
    }

    @Test
    public void itShouldGetData() {
        List<DeliveryRestDto> deliveries = sut.getData(ANY_CONTRACT_ID).execute();

        assertThat(deliveries, is(notNullValue()));
        assertThat(deliveries, hasSize(greaterThan(0)));

        DeliveryRestDto firstDelivery = deliveries.stream().findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(firstDelivery, is(notNullValue()));
        assertThat(firstDelivery.getDeliveryId(), is(greaterThanOrEqualTo(0L)));
        assertThat(firstDelivery.getTimestamp(), is(notNullValue()));
        assertThat(firstDelivery.getTrack(), is(notNullValue()));
        assertThat(firstDelivery.getTrack(), hasSize(greaterThan(0)));

        TrackRestDto anyTrack = firstDelivery.getTrack().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyTrack, is(notNullValue()));
        assertThat(anyTrack.getNodes(), is(notNullValue()));
        assertThat(anyTrack.getNodes(), hasSize(greaterThan(0)));

        NodeRestDto firstNode = anyTrack.getNodes().stream().findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(firstNode, is(notNullValue()));
        assertThat(firstNode.getId(), is(greaterThanOrEqualTo(0L)));
        assertThat(firstNode.getLatitude(), is(notNullValue()));
        assertThat(firstNode.getLongitude(), is(notNullValue()));
        assertThat(firstNode.getSpeed(), is(notNullValue()));
        assertThat(firstNode.getHdop(), is(notNullValue()));
        assertThat(firstNode.getVdop(), is(notNullValue()));
        assertThat(firstNode.getAltitude(), is(notNullValue()));
        assertThat(firstNode.getHeading(), is(notNullValue()));
        assertThat(firstNode.getSatellites(), is(greaterThanOrEqualTo(0)));
        assertThat(firstNode.getStates(), is(notNullValue()));
        assertThat(firstNode.getStates(), hasSize(greaterThan(0)));
        assertThat(firstNode.getXfcds(), is(notNullValue()));
        assertThat(firstNode.getXfcds(), hasSize(greaterThan(0)));
    }

    @Test
    public void itShouldGetLastData() {
        List<NodeRestDto> nodes = sut.getLastData(ANY_CONTRACT_ID, VALID_VEHICLE_IDS).execute();

        assertThat(nodes, is(notNullValue()));
        assertThat(nodes, hasSize(1));

        NodeRestDto firstNode = nodes.stream().findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(firstNode, is(notNullValue()));
        assertThat(firstNode.getId(), is(greaterThanOrEqualTo(0L)));
        assertThat(firstNode.getLatitude(), is(notNullValue()));
        assertThat(firstNode.getLongitude(), is(notNullValue()));
        assertThat(firstNode.getSpeed(), is(notNullValue()));
        assertThat(firstNode.getHdop(), is(notNullValue()));
        assertThat(firstNode.getVdop(), is(notNullValue()));
        assertThat(firstNode.getAltitude(), is(notNullValue()));
        assertThat(firstNode.getHeading(), is(notNullValue()));
        assertThat(firstNode.getSatellites(), is(greaterThanOrEqualTo(0)));
        assertThat(firstNode.getStates(), is(notNullValue()));
        assertThat(firstNode.getStates(), hasSize(greaterThan(0)));
        assertThat(firstNode.getXfcds(), is(notNullValue()));
        assertThat(firstNode.getXfcds(), hasSize(greaterThan(0)));

        ParameterRestDto firstXfcdParam = firstNode.getXfcds().stream().findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(firstXfcdParam, is(notNullValue()));
        assertThat(firstXfcdParam.getTimestamp(), is(notNullValue()));
        assertThat(firstXfcdParam.getParam(), is(notNullValue()));
        assertThat(firstXfcdParam.getValue(), is(notNullValue()));
        assertThat(firstXfcdParam.getLatitude(), is(notNullValue()));
        assertThat(firstXfcdParam.getLongitude(), is(notNullValue()));

        ParameterRestDto firstStateParam = firstNode.getStates().stream().findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(firstStateParam, is(notNullValue()));
        assertThat(firstStateParam.getTimestamp(), is(notNullValue()));
        assertThat(firstStateParam.getParam(), is(notNullValue()));
        assertThat(firstStateParam.getValue(), is(notNullValue()));
        assertThat(firstStateParam.getLatitude(), is(notNullValue()));
        assertThat(firstStateParam.getLongitude(), is(notNullValue()));
    }
}

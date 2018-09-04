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
import org.amv.trafficsoft.rest.ErrorInfoRestDtoMother;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.client.TrafficsoftException;
import org.amv.trafficsoft.rest.xfcd.model.*;
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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
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

        NodeRestDto nodeDto = NodeRestDtoMother.random();
        DeliveryRestDto deliveryDto = DeliveryRestDtoMother.random();

        String deliveryDtoListAsJson = jsonMapper.writeValueAsString(Lists.newArrayList(deliveryDto));
        String nodeDtoListAsJson = jsonMapper.writeValueAsString(Lists.newArrayList(nodeDto));

        String exceptionJson = jsonMapper.writeValueAsString(ErrorInfoRestDtoMother.random());

        MockClient mockClient = new MockClient()
                .add(HttpMethod.GET, String.format("/api/rest/v1/xfcd?contractId=%d", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body(exceptionJson, Charsets.UTF_8))
                .add(HttpMethod.POST, String.format("/api/rest/v1/xfcd/confirm?contractId=%d", ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .headers(Collections.emptyMap()))
                .ok(HttpMethod.POST, String.format("/api/rest/v1/xfcd?contractId=%d", ANY_CONTRACT_ID), deliveryDtoListAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/xfcd?contractId=%d", ANY_CONTRACT_ID), deliveryDtoListAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/xfcd/last?contractId=%d&%s", ANY_CONTRACT_ID, VALID_VEHICLE_IDS.stream()
                        .map(val -> "vehicleId=" + val)
                        .collect(Collectors.joining("&"))), nodeDtoListAsJson);

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
        AtomicReference<TrafficsoftException> trafficsoftExceptionRef = new AtomicReference<>();

        sut.getData(NON_EXISTING_CONTRACT_ID)
                .toObservable()
                .subscribeOn(testScheduler)
                .subscribe(new TestSubscriber<List<DeliveryRestDto>>() {
                    @Override
                    public void onError(Throwable e) {
                        assertThat(e, instanceOf(HystrixRuntimeException.class));
                        assertThat(e.getCause(), instanceOf(FeignException.class));
                        assertThat(e.getCause().getCause(), instanceOf(TrafficsoftException.class));

                        trafficsoftExceptionRef.set((TrafficsoftException) e.getCause().getCause());

                        latch.countDown();
                    }

                    @Override
                    public void onNext(List<DeliveryRestDto> deliveryDtos) {
                        latch.countDown();

                        Assert.fail("Should have thrown exception and called onError");
                    }
                });

        testScheduler.triggerActions();

        latch.await(1, TimeUnit.SECONDS);

        TrafficsoftException trafficsoftException = trafficsoftExceptionRef.get();
        assertThat(trafficsoftException, is(notNullValue()));

        ErrorInfo errorInfo = trafficsoftException.getErrorInfo();
        assertThat(errorInfo, is(notNullValue()));
        assertThat(errorInfo.getId(), is(notNullValue()));
        assertThat(errorInfo.getDateTime(), is(notNullValue()));
        assertThat(errorInfo.getErrorCode(), is(notNullValue()));
        assertThat(errorInfo.getException(), is(notNullValue()));
        assertThat(errorInfo.getMessage(), is(notNullValue()));
        assertThat(errorInfo.getUrl(), is(notNullValue()));
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
    public void itShouldGetLatestData() {
        List<NodeRestDto> nodes = sut.getLatestData(ANY_CONTRACT_ID, VALID_VEHICLE_IDS).execute();

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

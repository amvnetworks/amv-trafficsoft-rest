package org.amv.trafficsoft.rest.client.carsharing.whitelist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import feign.Response;
import feign.Target;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.ErrorInfo;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.FetchWhitelistsResponseRestDto;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.UpdateWhitelistsRequestRestDto;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.VehicleWhitelistRestDto;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.client.TrafficsoftException;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarSharingWhitelistClientIT {
    private static final long NON_EXISTING_CONTRACT_ID = -1L;
    private static final long ANY_CONTRACT_ID = RandomUtils.nextLong();
    private static final List<Long> VALID_VEHICLE_IDS = ImmutableList.of(RandomUtils.nextLong(), RandomUtils.nextLong());

    private TestScheduler testScheduler = Schedulers.test();

    private MockClient mockClient;

    private CarSharingWhitelistClient sut;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper jsonMapper = ClientConfig.ConfigurableClientConfig.defaultObjectMapper;

        FetchWhitelistsResponseRestDto fetchWhitelistsResponseRestDto = FetchWhitelistsResponseRestDto.builder()
                .vehicleWhitelists(VALID_VEHICLE_IDS.stream()
                        .map(vehicleId -> VehicleWhitelistRestDto.builder()
                                .vehicleId(vehicleId)
                                .whitelist(IntStream.range(0, RandomUtils.nextInt(9) + 1).boxed()
                                        .map(i -> RandomStringUtils.randomAlphanumeric(8))
                                        .collect(Collectors.toList()))
                                .build()).collect(toList()))
                .build();

        String retrieveWhitelistResponseRestDtoAsJson = jsonMapper.writeValueAsString(fetchWhitelistsResponseRestDto);

        String exceptionJson = jsonMapper.writeValueAsString(ErrorInfo.builder()
                .id(RandomStringUtils.randomAlphanumeric(6))
                .dateTime(LocalDateTime.now())
                .errorCode(RandomStringUtils.randomNumeric(6))
                .exception(RandomStringUtils.randomAlphanumeric(10))
                .message(RandomStringUtils.randomAlphanumeric(10))
                .url(RandomStringUtils.randomAlphanumeric(10))
                .build());

        String queryString = VALID_VEHICLE_IDS.stream()
                .map(vehicleId -> String.format("vehicleId=%s", vehicleId))
                .collect(joining("&"));

        this.mockClient = new MockClient()
                .add(HttpMethod.POST, String.format("/%d/car-sharing/whitelist", ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .headers(Collections.emptyMap()))
                .add(HttpMethod.GET, String.format("/%d/car-sharing/whitelist", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body(exceptionJson, Charsets.UTF_8))
                .ok(HttpMethod.GET, String.format("/%d/car-sharing/whitelist?%s", ANY_CONTRACT_ID, queryString), retrieveWhitelistResponseRestDtoAsJson);

        Target<CarSharingWhitelistClient> mockTarget = new MockTarget<>(CarSharingWhitelistClient.class);

        ClientConfig<CarSharingWhitelistClient> config = ClientConfig.ConfigurableClientConfig.<CarSharingWhitelistClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        this.sut = TrafficsoftClients.carSharingWhitelist(config);
    }

    @Test
    public void itShouldUpdateWhitelists() throws Exception {
        UpdateWhitelistsRequestRestDto updateWhitelistRequest = UpdateWhitelistsRequestRestDto.builder()
                .vehicleWhitelists(VALID_VEHICLE_IDS.stream()
                        .map(vehicleId -> VehicleWhitelistRestDto.builder()
                                .vehicleId(vehicleId)
                                .whitelist(IntStream.range(0, RandomUtils.nextInt(9) + 1).boxed()
                                        .map(i -> RandomStringUtils.randomAlphanumeric(8))
                                        .collect(Collectors.toList()))
                                .build()).collect(toList()))
                .build();

        Void returnValue = this.sut.updateWhitelists(ANY_CONTRACT_ID, updateWhitelistRequest).execute();

        assertThat(returnValue, is(nullValue()));

        String url = String.format("/%s/car-sharing/whitelist", ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.POST, url);
    }

    @Test
    public void itShouldFetchWhitelists() throws Exception {
        FetchWhitelistsResponseRestDto returnValue = this.sut.fetchWhitelists(ANY_CONTRACT_ID, VALID_VEHICLE_IDS).execute();

        assertThat(returnValue, is(notNullValue()));

        String queryString = VALID_VEHICLE_IDS.stream()
                .map(vehicleId -> String.format("vehicleId=%s", vehicleId))
                .collect(joining("&"));
        String url = String.format("/%s/car-sharing/whitelist?%s", ANY_CONTRACT_ID, queryString);
        this.mockClient.verifyOne(HttpMethod.GET, url);
    }


    @Test
    public void itShouldThrowExceptionOnMismatchingContractId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<TrafficsoftException> trafficsoftExceptionRef = new AtomicReference<>();

        sut.fetchWhitelists(NON_EXISTING_CONTRACT_ID, Collections.emptyList())
                .toObservable()
                .subscribeOn(testScheduler)
                .subscribe(new TestSubscriber<FetchWhitelistsResponseRestDto>() {
                    @Override
                    public void onError(Throwable e) {
                        assertThat(e, instanceOf(HystrixRuntimeException.class));
                        assertThat(e.getCause(), instanceOf(TrafficsoftException.class));
                        assertThat(e.getCause().getCause(), instanceOf(FeignException.class));

                        trafficsoftExceptionRef.set((TrafficsoftException) e.getCause());

                        latch.countDown();
                    }

                    @Override
                    public void onNext(FetchWhitelistsResponseRestDto response) {
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
}
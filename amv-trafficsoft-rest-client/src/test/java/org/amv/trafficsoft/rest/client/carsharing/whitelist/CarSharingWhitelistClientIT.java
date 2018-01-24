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
import org.amv.trafficsoft.rest.ErrorInfoRestDtoMother;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.*;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.client.TrafficsoftException;
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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.joining;
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

        FetchWhitelistsResponseRestDto fetchWhitelistsResponseRestDto = FetchWhitelistsResponseRestDtoMother.randomForVehicleIds(VALID_VEHICLE_IDS);

        String retrieveWhitelistResponseRestDtoAsJson = jsonMapper.writeValueAsString(fetchWhitelistsResponseRestDto);

        String exceptionJson = jsonMapper.writeValueAsString(ErrorInfoRestDtoMother.random());

        String queryString = VALID_VEHICLE_IDS.stream()
                .map(vehicleId -> String.format("vehicleId=%s", vehicleId))
                .collect(joining("&"));

        this.mockClient = new MockClient()
                .add(HttpMethod.POST, String.format("/api/rest/v1/car-sharing/whitelist?contractId=%d", ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .headers(Collections.emptyMap()))
                .add(HttpMethod.GET, String.format("/api/rest/v1/car-sharing/whitelist?contractId=%d", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body(exceptionJson, Charsets.UTF_8))
                .ok(HttpMethod.GET, String.format("/api/rest/v1/car-sharing/whitelist?contractId=%d&%s", ANY_CONTRACT_ID, queryString), retrieveWhitelistResponseRestDtoAsJson);

        Target<CarSharingWhitelistClient> mockTarget = new MockTarget<>(CarSharingWhitelistClient.class);

        ClientConfig<CarSharingWhitelistClient> config = ClientConfig.ConfigurableClientConfig.<CarSharingWhitelistClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        this.sut = TrafficsoftClients.carSharingWhitelist(config);
    }

    @Test
    public void itShouldUpdateWhitelists() throws Exception {
        UpdateWhitelistsRequestRestDto updateWhitelistRequest = UpdateWhitelistsRequestRestDtoMother.randomForVehicleIds(VALID_VEHICLE_IDS);

        Void returnValue = this.sut.updateWhitelists(ANY_CONTRACT_ID, updateWhitelistRequest).execute();

        assertThat(returnValue, is(nullValue()));

        String url = String.format("/api/rest/v1/car-sharing/whitelist?contractId=%d", ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.POST, url);
    }

    @Test
    public void itShouldFetchWhitelists() throws Exception {
        FetchWhitelistsResponseRestDto returnValue = this.sut.fetchWhitelists(ANY_CONTRACT_ID, VALID_VEHICLE_IDS).execute();

        assertThat(returnValue, is(notNullValue()));

        checkArgument(VALID_VEHICLE_IDS.size() >= 2, "Sanity check");

        List<VehicleWhitelistRestDto> vehicleWhitelists = returnValue.getVehicleWhitelists();
        assertThat(vehicleWhitelists, hasSize(VALID_VEHICLE_IDS.size()));
        assertThat(vehicleWhitelists.get(0), is(notNullValue()));
        assertThat(vehicleWhitelists.get(0).getVehicleId(), is(VALID_VEHICLE_IDS.get(0)));
        assertThat(vehicleWhitelists.get(0).getWhitelist(), is(notNullValue()));
        assertThat(vehicleWhitelists.get(0).getWhitelist(), hasSize(greaterThanOrEqualTo(1)));

        assertThat(vehicleWhitelists.get(1), is(notNullValue()));
        assertThat(vehicleWhitelists.get(1).getVehicleId(), is(VALID_VEHICLE_IDS.get(1)));
        assertThat(vehicleWhitelists.get(1).getWhitelist(), is(notNullValue()));
        assertThat(vehicleWhitelists.get(1).getWhitelist(), hasSize(greaterThanOrEqualTo(1)));


        String queryString = VALID_VEHICLE_IDS.stream()
                .map(vehicleId -> String.format("vehicleId=%s", vehicleId))
                .collect(joining("&"));
        String url = String.format("/api/rest/v1/car-sharing/whitelist?contractId=%d&%s", ANY_CONTRACT_ID, queryString);
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

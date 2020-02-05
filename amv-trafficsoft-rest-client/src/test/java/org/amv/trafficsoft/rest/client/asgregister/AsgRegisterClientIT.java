package org.amv.trafficsoft.rest.client.asgregister;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import feign.RequestInterceptor;
import feign.Response;
import feign.Target;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.ErrorInfo;
import org.amv.trafficsoft.rest.ErrorInfoRestDtoMother;
import org.amv.trafficsoft.rest.asgregister.model.*;
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
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsgRegisterClientIT {
    private static final long NON_EXISTING_CONTRACT_ID = -1L;
    private static final long ANY_CONTRACT_ID = RandomUtils.nextLong();
    private static final String ANY_OEM_CODE = RandomStringUtils.randomAlphanumeric(10);
    private static final String ANY_SERIES_CODE = RandomStringUtils.randomAlphanumeric(10);
    private static final String ANY_MODEL_CODE = RandomStringUtils.randomAlphanumeric(10);
    private static final long ANY_VEHICLE_ID = RandomUtils.nextLong();
    private static final String ANY_VEHICLE_KEY = RandomStringUtils.randomAlphanumeric(10);

    private TestScheduler testScheduler = Schedulers.test();

    private AsgRegisterClient sut;

    @Before
    public void setUp() throws JsonProcessingException {
        this.sut = configureAsgRegisterClient();
    }

    @Test
    public void itShouldThrowExceptionOnMismatchingContractId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<TrafficsoftException> trafficsoftExceptionRef = new AtomicReference<>();
        sut.registerAsg(NON_EXISTING_CONTRACT_ID, RegisterAsgRequestRestDto.builder().build())
                .toObservable()
                .subscribeOn(testScheduler)
                .subscribe(new TestSubscriber<RegisterAsgResponseRestDto>() {
                    @Override
                    public void onError(Throwable e) {
                        assertThat(e, instanceOf(HystrixRuntimeException.class));
                        assertThat(e.getCause(), instanceOf(FeignException.class));
                        assertThat(e.getCause().getCause(), instanceOf(TrafficsoftException.class));
                        trafficsoftExceptionRef.set((TrafficsoftException) e.getCause().getCause());
                        latch.countDown();
                    }

                    @Override
                    public void onNext(RegisterAsgResponseRestDto registerAsgResponseRestDto) {
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
    public void itShouldRegisterAsg() {
        RegisterAsgRequestRestDto registerAsgRequest = RegisterAsgRequestRestDto.builder()
                .vehicleKey(ANY_VEHICLE_KEY)
                .oemCode(ANY_OEM_CODE)
                .seriesCode(ANY_SERIES_CODE)
                .modelCode(ANY_MODEL_CODE)
                .build();

        RegisterAsgResponseRestDto registerAsgResponse = sut.registerAsg(ANY_CONTRACT_ID, registerAsgRequest)
                .execute();

        assertThat(registerAsgResponse, is(notNullValue()));
        VehicleRestDto vehicle = registerAsgResponse.getVehicle();

        assertThat(vehicle, is(notNullValue()));
        assertThat(vehicle.getId(), is(equalTo(ANY_VEHICLE_ID)));
        assertThat(vehicle.getOemCode(), is(equalTo(registerAsgRequest.getOemCode())));
        assertThat(vehicle.getSeriesCode(), is(equalTo(registerAsgRequest.getSeriesCode())));
        assertThat(vehicle.getModelCode(), is(equalTo(registerAsgRequest.getModelCode())));

    }

    @Test
    public void itShouldLoadVehicle() {
        VehicleResponseRestDto vehicleResponse = sut.getVehicle(ANY_CONTRACT_ID, ANY_VEHICLE_ID)
                .execute();

        assertThat(vehicleResponse, is(notNullValue()));

        VehicleRestDto vehicle = vehicleResponse.getVehicle();
        assertThat(vehicle, is(notNullValue()));
        assertThat(vehicle.getOemCode(), is(equalTo(ANY_OEM_CODE)));
        assertThat(vehicle.getSeriesCode(), is(equalTo(ANY_SERIES_CODE)));
        assertThat(vehicle.getModelCode(), is(equalTo(ANY_MODEL_CODE)));
    }

    @Test
    public void itShouldLoadVehicleKey() {
        VehicleKeyResponseRestDto vehicleKeyResponse = sut.getVehicleKey(ANY_CONTRACT_ID, ANY_VEHICLE_KEY)
                .execute();

        assertThat(vehicleKeyResponse, is(notNullValue()));

        VehicleKeyRestDto vehicleKey = vehicleKeyResponse.getVehicleKey();
        assertThat(vehicleKey, is(notNullValue()));
        assertThat(vehicleKey.getKey(), is(equalTo(ANY_VEHICLE_KEY)));
        assertThat(vehicleKey.getVehicleId(), is(equalTo(ANY_VEHICLE_ID)));
        assertThat(vehicleKey.isValid(), is(true));
    }

    @Test
    public void itShouldLoadAllOems() {
        OemsResponseRestDto oemsResponseDto = sut
                .getOems(ANY_CONTRACT_ID)
                .execute();

        assertThat(oemsResponseDto, is(notNullValue()));
        assertThat(oemsResponseDto.getOems(), hasSize(greaterThan(0)));

        OemRestDto anyOem = oemsResponseDto.getOems().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyOem, is(notNullValue()));
        assertThat(anyOem.getOemCode(), is(notNullValue()));
    }

    @Test
    public void itShouldLoadAllSeriesForOem() {
        OemRestDto anyOem = sut.getOems(ANY_CONTRACT_ID).execute()
                .getOems().stream()
                .findAny()
                .orElseThrow(IllegalStateException::new);

        SeriesResponseRestDto seriesResponseDto = sut
                .getSeries(ANY_CONTRACT_ID, anyOem.getOemCode())
                .execute();

        assertThat(seriesResponseDto, is(notNullValue()));
        assertThat(seriesResponseDto.getSeries(), hasSize(greaterThan(0)));

        SeriesRestDto anySeries = seriesResponseDto.getSeries().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anySeries, is(notNullValue()));
        assertThat(anySeries.getSeriesCode(), is(notNullValue()));
        assertThat(anySeries.getOemCode(), is(equalTo(anyOem.getOemCode())));
    }

    @Test
    public void itShouldLoadAllModelsForOemAndSeries() {
        SeriesRestDto anySeries = sut.getOems(ANY_CONTRACT_ID).execute()
                .getOems().stream()
                .findAny()
                .map(anyOem -> sut.getSeries(ANY_CONTRACT_ID, anyOem.getOemCode()))
                .map(HystrixCommand::execute)
                .flatMap(seriesResponseDto -> seriesResponseDto.getSeries().stream().findAny())
                .orElseThrow(IllegalStateException::new);


        ModelsResponseRestDto modelsResponseDto = sut
                .getModels(ANY_CONTRACT_ID, anySeries.getOemCode(), anySeries.getSeriesCode())
                .execute();

        assertThat(modelsResponseDto, is(notNullValue()));
        assertThat(modelsResponseDto.getModels(), hasSize(greaterThan(0)));

        ModelRestDto anyModel = modelsResponseDto.getModels().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyModel, is(notNullValue()));
        assertThat(anyModel.getModelCode(), is(notNullValue()));
        assertThat(anyModel.getSeriesCode(), is(equalTo(anySeries.getSeriesCode())));
        assertThat(anyModel.getOemCode(), is(equalTo(anySeries.getOemCode())));
    }

    @Test
    public void itShouldLoadAllModelsForOemAndSeriesByParams() {
        SeriesRestDto anySeries = sut.getOems(ANY_CONTRACT_ID).execute()
                .getOems().stream()
                .findAny()
                .map(anyOem -> sut.getSeries(ANY_CONTRACT_ID, anyOem.getOemCode()))
                .map(HystrixCommand::execute)
                .flatMap(seriesResponseDto -> seriesResponseDto.getSeries().stream().findAny())
                .orElseThrow(IllegalStateException::new);


        ModelsResponseRestDto modelsResponseDto = this.sut
                .getModelsByParams(ANY_CONTRACT_ID, Arrays.asList(new String[]{"kmrd", "skmrd"}), anySeries.getOemCode(), anySeries.getSeriesCode())
                .execute();

        assertThat(modelsResponseDto, is(notNullValue()));
        assertThat(modelsResponseDto.getModels(), hasSize(greaterThan(0)));

        ModelRestDto anyModel = modelsResponseDto.getModels().stream().findAny()
                .orElseThrow(IllegalStateException::new);

        assertThat(anyModel, is(notNullValue()));
        assertThat(anyModel.getModelCode(), is(notNullValue()));
        assertThat(anyModel.getSeriesCode(), is(equalTo(anySeries.getSeriesCode())));
        assertThat(anyModel.getOemCode(), is(equalTo(anySeries.getOemCode())));
    }

    private static AsgRegisterClient configureAsgRegisterClient() throws JsonProcessingException {
        ObjectMapper jsonMapper = ClientConfig.ConfigurableClientConfig.defaultObjectMapper;

        String registerAsgResponseDtoAsJson = jsonMapper.writeValueAsString(RegisterAsgResponseRestDto.builder()
                .vehicle(VehicleRestDto.builder()
                        .id(ANY_VEHICLE_ID)
                        .oemCode(ANY_OEM_CODE)
                        .seriesCode(ANY_SERIES_CODE)
                        .modelCode(ANY_MODEL_CODE)
                        .build())
                .build());

        String oemsResponseDtoAsJson = jsonMapper.writeValueAsString(OemsResponseRestDto.builder()
                .addOem(OemRestDto.builder()
                        .oemCode(ANY_OEM_CODE)
                        .build())
                .build());

        String seriesResponseDtoAsJson = jsonMapper.writeValueAsString(SeriesResponseRestDto.builder()
                .addSeries(SeriesRestDto.builder()
                        .oemCode(ANY_OEM_CODE)
                        .seriesCode(ANY_SERIES_CODE)
                        .build())
                .build());

        ModelsResponseRestDto modelResp = ModelsResponseRestDto.builder()
                .addModel(ModelRestDto.builder()
                        .oemCode(ANY_OEM_CODE)
                        .seriesCode(ANY_SERIES_CODE)
                        .modelCode(ANY_MODEL_CODE)
                        .build())
                .build();

        String modelResponseDtoAsJson = jsonMapper.writeValueAsString(modelResp);

        String vehicleResponseDtoAsJson = jsonMapper.writeValueAsString(VehicleResponseRestDto.builder()
                .vehicle(VehicleRestDto.builder()
                        .oemCode(ANY_OEM_CODE)
                        .seriesCode(ANY_SERIES_CODE)
                        .modelCode(ANY_MODEL_CODE)
                        .build())
                .build());

        String vehicleKeyResponseDtoAsJson = jsonMapper.writeValueAsString(VehicleKeyResponseRestDto.builder()
                .vehicleKey(VehicleKeyRestDto.builder()
                        .key(ANY_VEHICLE_KEY)
                        .vehicleId(ANY_VEHICLE_ID)
                        .valid(true)
                        .build())
                .build());

        String exceptionJson = jsonMapper.writeValueAsString(ErrorInfoRestDtoMother.random());

        MockClient mockClient = new MockClient()
                .add(HttpMethod.POST, String.format("/api/rest/v1/asg-register?contractId=%d", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body(exceptionJson, Charsets.UTF_8))
                .ok(HttpMethod.POST, String.format("/api/rest/v1/asg-register?contractId=%d", ANY_CONTRACT_ID), registerAsgResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/asg-register/oem?contractId=%d", ANY_CONTRACT_ID), oemsResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/asg-register/oem/%s/series?contractId=%d", ANY_OEM_CODE, ANY_CONTRACT_ID), seriesResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/asg-register/oem/%s/series/%s/model?contractId=%d", ANY_OEM_CODE, ANY_SERIES_CODE, ANY_CONTRACT_ID), modelResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/asg-register/oem/%s/series/%s/model?contractId=%d&params=kmrd&params=skmrd", ANY_OEM_CODE, ANY_SERIES_CODE, ANY_CONTRACT_ID), modelResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/asg-register/vehicle/%d?contractId=%d", ANY_VEHICLE_ID, ANY_CONTRACT_ID), vehicleResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/api/rest/v1/asg-register/vehiclekey/%s?contractId=%d", ANY_VEHICLE_KEY, ANY_CONTRACT_ID), vehicleKeyResponseDtoAsJson);

        Target<AsgRegisterClient> mockTarget = new MockTarget<>(AsgRegisterClient.class);

        ClientConfig<AsgRegisterClient> config = ClientConfig.ConfigurableClientConfig.<AsgRegisterClient>builder()
                .requestInterceptors(TrafficsoftClients.getRequestInterceptors())
                .client(mockClient)
                .target(mockTarget)
                .build();

        return TrafficsoftClients.asgRegister(config);
    }
}

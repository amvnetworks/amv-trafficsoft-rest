package org.amv.trafficsoft.rest.client.asgregister;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import feign.Response;
import feign.Target;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.asgregister.model.*;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
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

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsgRegisterClientIT {
    private static final long NON_EXISTING_CONTRACT_ID = -1L;
    private static final long ANY_CONTRACT_ID = RandomUtils.nextLong();
    private static final String ANY_OEM_CODE = "AUDI";
    private static final String ANY_SERIES_CODE = "A1";
    private static final String ANY_MODEL_CODE = "A1S2";
    private static final long ANY_VEHICLE_ID = RandomUtils.nextLong();
    private static final String ANY_VEHICLE_KEY = RandomStringUtils.randomAlphanumeric(10);

    private TestScheduler testScheduler = Schedulers.test();

    private AsgRegisterClient sut;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper jsonMapper = new ObjectMapper();

        String registerAsgResponseRestDtoAsJson = jsonMapper.writeValueAsString(RegisterAsgResponseRestDto.builder()
                .build());
        String oemsResponseDtoAsJson = jsonMapper.writeValueAsString(OemsResponseDto.builder()
                .addOem(OemRestDto.builder()
                        .oemCode(ANY_OEM_CODE)
                        .build())
                .build());
        String seriesResponseDtoAsJson = jsonMapper.writeValueAsString(SeriesResponseDto.builder()
                .addSeries(SeriesRestDto.builder()
                        .oemCode(ANY_OEM_CODE)
                        .seriesCode(ANY_SERIES_CODE)
                        .build())
                .build());
        String modelResponseDtoAsJson = jsonMapper.writeValueAsString(ModelsResponseDto.builder()
                .addModel(ModelRestDto.builder()
                        .oemCode(ANY_OEM_CODE)
                        .seriesCode(ANY_SERIES_CODE)
                        .modelCode(ANY_MODEL_CODE)
                        .build())
                .build());

        String vehicleResponseDtoAsJson = jsonMapper.writeValueAsString(VehicleResponseDto.builder()
                .build());

        String vehicleKeyResponseDtoAsJson = jsonMapper.writeValueAsString(VehicleKeyResponseDto.builder()
                .build());

        MockClient mockClient = new MockClient()
                .add(HttpMethod.POST, String.format("/%d/asg-register", NON_EXISTING_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .headers(Collections.emptyMap())
                        .body("{}", Charsets.UTF_8)
                        .build())
                .ok(HttpMethod.POST, String.format("/%d/asg-register", ANY_CONTRACT_ID), registerAsgResponseRestDtoAsJson)
                .ok(HttpMethod.GET, String.format("/%d/asg-register/oem", ANY_CONTRACT_ID), oemsResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/%d/asg-register/oem/%s/series", ANY_CONTRACT_ID, ANY_OEM_CODE), seriesResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/%d/asg-register/oem/%s/series/%s/model", ANY_CONTRACT_ID, ANY_OEM_CODE, ANY_SERIES_CODE), modelResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/%d/asg-register/vehicle/%d", ANY_CONTRACT_ID, ANY_VEHICLE_ID), vehicleResponseDtoAsJson)
                .ok(HttpMethod.GET, String.format("/%d/asg-register/vehiclekey/%s", ANY_CONTRACT_ID, ANY_VEHICLE_KEY), vehicleKeyResponseDtoAsJson);

        Target<AsgRegisterClient> mockTarget = new MockTarget<>(AsgRegisterClient.class);

        ClientConfig<AsgRegisterClient> config = ClientConfig.ConfigurableClientConfig.<AsgRegisterClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        this.sut = TrafficsoftClients.asgRegister(config);
    }

    @Test
    public void itShouldThrowExceptionOnMismatchingContractId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean onErrorCalled = new AtomicBoolean(false);
        sut.registerAsg(NON_EXISTING_CONTRACT_ID, RegisterAsgRequestRestDto.builder().build())
                .toObservable()
                .subscribeOn(testScheduler)
                .subscribe(new TestSubscriber<RegisterAsgResponseRestDto>() {
                    @Override
                    public void onError(Throwable e) {
                        assertThat(e, instanceOf(HystrixRuntimeException.class));
                        assertThat(e.getCause(), instanceOf(FeignException.class));
                        onErrorCalled.set(true);
                        latch.countDown();
                    }

                    @Override
                    public void onNext(RegisterAsgResponseRestDto registerAsgResponseRestDto) {
                        Assert.fail("Should have thrown exception and called onError");
                        latch.countDown();
                    }
                });

        testScheduler.triggerActions();

        latch.await();

        assertThat(onErrorCalled.get(), is(true));
    }

    @Test
    public void itShouldRegisterAsg() {
        // TODO: implement me
    }

    @Test
    public void itShouldLoadVehicle() {
        // TODO: implement me
    }

    @Test
    public void itShouldLoadVehicleKey() {
        // TODO: implement me
    }

    @Test
    public void itShouldLoadAllOems() {
        OemsResponseDto oemsResponseDto = sut
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

        SeriesResponseDto seriesResponseDto = sut
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


        ModelsResponseDto modelsResponseDto = sut
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
}

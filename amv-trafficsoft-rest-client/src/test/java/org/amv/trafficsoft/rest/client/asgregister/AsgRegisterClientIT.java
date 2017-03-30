package org.amv.trafficsoft.rest.client.asgregister;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.amv.trafficsoft.rest.asgregister.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsgRegisterClientIT {
    private static final long NON_EXISTING_CONTRACT_ID = -1L;
    private static final long VALID_CONTRACT_ID = 16075364L;

    @Autowired
    private AsgRegisterClient asgRegisterClient;

    private TestScheduler testScheduler = Schedulers.test();

    @Test
    public void itShouldThrowExceptionOnMismatchingContractId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean onErrorCalled = new AtomicBoolean(false);
        asgRegisterClient.getOems(NON_EXISTING_CONTRACT_ID)
                .toObservable()
                .subscribeOn(testScheduler)
                .subscribe(new TestSubscriber<OemsResponseDto>() {
                    @Override
                    public void onError(Throwable e) {
                        assertThat(e, instanceOf(HystrixRuntimeException.class));
                        assertThat(e.getCause(), instanceOf(FeignException.class));
                        onErrorCalled.set(true);
                        latch.countDown();
                    }

                    @Override
                    public void onNext(OemsResponseDto oemsResponseDto) {
                        Assert.fail("Should have thrown exception and called onError");
                        latch.countDown();
                    }
                });

        testScheduler.triggerActions();

        latch.await();

        assertThat(onErrorCalled.get(), is(true));
    }

    @Test
    public void itShouldLoadAllOems() {
        OemsResponseDto oemsResponseDto = asgRegisterClient
                .getOems(VALID_CONTRACT_ID)
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
        OemRestDto anyOem = asgRegisterClient.getOems(VALID_CONTRACT_ID).execute()
                .getOems().stream()
                .findAny()
                .orElseThrow(IllegalStateException::new);

        SeriesResponseDto seriesResponseDto = asgRegisterClient
                .getSeries(VALID_CONTRACT_ID, anyOem.getOemCode())
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
        SeriesRestDto anySeries = asgRegisterClient.getOems(VALID_CONTRACT_ID).execute()
                .getOems().stream()
                .findAny()
                .map(anyOem -> asgRegisterClient.getSeries(VALID_CONTRACT_ID, anyOem.getOemCode()))
                .map(HystrixCommand::execute)
                .flatMap(seriesResponseDto -> seriesResponseDto.getSeries().stream().findAny())
                .orElseThrow(IllegalStateException::new);


        ModelsResponseDto modelsResponseDto = asgRegisterClient
                .getModels(VALID_CONTRACT_ID, anySeries.getOemCode(), anySeries.getSeriesCode())
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

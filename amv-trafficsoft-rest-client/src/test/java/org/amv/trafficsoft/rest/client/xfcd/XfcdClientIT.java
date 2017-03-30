package org.amv.trafficsoft.rest.client.xfcd;

import com.google.common.collect.ImmutableList;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.amv.trafficsoft.rest.xfcd.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

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
    private static final long VALID_CONTRACT_ID = 16075364L;
    private static final List<Long> VALID_VEHICLE_IDS = ImmutableList.of(1041L, 1045L);

    @Autowired
    private XfcdClient xfcdClient;

    private TestScheduler testScheduler = Schedulers.test();

    @Test
    public void itShouldThrowExceptionOnMismatchingContractId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean onErrorCalled = new AtomicBoolean(false);
        xfcdClient.getData(NON_EXISTING_CONTRACT_ID)
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
    public void itShouldGetData() {
        List<DeliveryDto> deliveries = xfcdClient.getData(VALID_CONTRACT_ID).execute();

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
    public void itShouldReturnEmptyListOnGetLastDataWithoutVehicleIds() {
        List<NodeDto> nodes = xfcdClient.getLastData(VALID_CONTRACT_ID, Collections.emptyList()).execute();

        assertThat(nodes, is(notNullValue()));
    }

    @Test
    public void itShouldGetLastData() {
        List<NodeDto> nodes = xfcdClient.getLastData(VALID_CONTRACT_ID, VALID_VEHICLE_IDS).execute();

        assertThat(nodes, is(notNullValue()));
    }
}

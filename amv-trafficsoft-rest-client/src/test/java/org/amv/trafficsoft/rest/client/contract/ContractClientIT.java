package org.amv.trafficsoft.rest.client.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import feign.Response;
import feign.Target;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.contract.datapackage.model.*;
import org.amv.trafficsoft.rest.contract.subscription.model.SubscriptionRestDto;
import org.amv.trafficsoft.rest.contract.subscription.model.SubscriptionsResponseRestDto;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractClientIT {
    private static final long ANY_CONTRACT_ID = RandomUtils.nextLong();
    private static final long ANY_VEHICLE_ID = RandomUtils.nextLong();

    private MockClient mockClient;

    private ContractClient sut;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper jsonMapper = ClientConfig.ConfigurableClientConfig.defaultObjectMapper;

        String datapackageResponseJson = jsonMapper.writeValueAsString(DataPackageResponseRestDto.builder()
                .gpsClean(true)
                .params(ParamsRestDto.builder()
                        .addDopuxParam(DopuxParamRestDto.builder()
                                .code("DOPUX_CODE")
                                .description("DOPUX_DESC")
                                .name("DOPUX_NAME")
                                .build())
                        .addStateParam(StateParamRestDto.builder()
                                .code("STATE_CODE")
                                .description("STATE_DESC")
                                .name("STATE_NAME")
                                .build())
                        .addXfcdParam(XfcdParamRestDto.builder()
                                .code("XFCD_CODE")
                                .description("XFCD_DESC")
                                .name("XFCD_NAME")
                                .build())
                        .build())
                .build());

        String subscriptionResponseJson = jsonMapper.writeValueAsString(SubscriptionsResponseRestDto.builder()
                .addSubscription(SubscriptionRestDto.builder()
                        .from(Date.from(Instant.now()))
                        .until(Date.from(Instant.now().plusSeconds(13 + RandomUtils.nextInt())))
                        .vehicleId(ANY_VEHICLE_ID)
                        .build())
                .build());

        this.mockClient = new MockClient()
                .add(HttpMethod.GET, String.format("/api/rest/v1/contract/%d/datapackage?contractId=%d", ANY_CONTRACT_ID, ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .body(datapackageResponseJson, Charsets.UTF_8)
                        .headers(Collections.emptyMap()))
                .add(HttpMethod.GET, String.format("/api/rest/v1/contract/%d/subscription?contractId=%d", ANY_CONTRACT_ID, ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .body(subscriptionResponseJson, Charsets.UTF_8)
                        .headers(Collections.emptyMap()));


        Target<ContractClient> mockTarget = new MockTarget<>(ContractClient.class);

        ClientConfig<ContractClient> config = ClientConfig.ConfigurableClientConfig.<ContractClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        this.sut = TrafficsoftClients.contract(config);
    }

    @Test
    public void itShouldFetchDatapackage() throws Exception {
        DataPackageResponseRestDto returnValue = this.sut.fetchDataPackage(ANY_CONTRACT_ID, "de").execute();

        assertThat(returnValue, is(notNullValue()));
        assertThat(returnValue.isGpsClean(), is(true));
        assertThat(returnValue.getParams(), is(notNullValue()));
        assertThat(returnValue.getParams().getDopux(), hasSize(1));
        assertThat(returnValue.getParams().getStates(), hasSize(1));
        assertThat(returnValue.getParams().getXfcds(), hasSize(1));

        String url = String.format("/api/rest/v1/contract/%d/datapackage?contractId=%d", ANY_CONTRACT_ID, ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.GET, url);
    }

    @Test
    public void itShouldFetchSubscriptions() throws Exception {
        SubscriptionsResponseRestDto returnValue = this.sut.fetchSubscriptions(ANY_CONTRACT_ID).execute();

        assertThat(returnValue, is(notNullValue()));
        assertThat(returnValue.getSubscriptions(), hasSize(1));

        SubscriptionRestDto subscription = returnValue.getSubscriptions().get(0);
        assertThat(subscription, is(notNullValue()));
        assertThat(subscription.getVehicleId(), is(ANY_VEHICLE_ID));
        assertThat(subscription.getFrom(), is(notNullValue()));
        assertThat(subscription.getUntil(), is(notNullValue()));
        assertThat(subscription.getFrom().toInstant().isBefore(subscription.getUntil().toInstant()), is(true));

        String url = String.format("/api/rest/v1/contract/%d/subscription?contractId=%d", ANY_CONTRACT_ID, ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.GET, url);
    }

}

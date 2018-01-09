package org.amv.trafficsoft.rest.client.carsharing.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import feign.Response;
import feign.Target;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.ErrorInfoRestDtoMother;
import org.amv.trafficsoft.rest.carsharing.reservation.model.*;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarSharingReservationClientIT {
    private static final long ANY_RESERVATION_ID = RandomUtils.nextLong();
    private static final long ANY_CONTRACT_ID = RandomUtils.nextLong();
    private static final long ANY_VEHICLE_ID = RandomUtils.nextLong();
    private static final List<Long> VALID_VEHICLE_IDS = ImmutableList.of(RandomUtils.nextLong(), RandomUtils.nextLong());

    private MockClient mockClient;

    private CarSharingReservationClient sut;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper jsonMapper = ClientConfig.ConfigurableClientConfig.defaultObjectMapper;

        String createReservationResponseJson = jsonMapper.writeValueAsString(ReservationRestDtoMother.randomWithVehicleId(ANY_VEHICLE_ID));
        String fetchReservationResponseJson = jsonMapper.writeValueAsString(ImmutableList.builder()
                .add(ReservationRestDtoMother.randomWithVehicleId(ANY_VEHICLE_ID))
                .add(ReservationRestDtoMother.randomWithVehicleId(ANY_VEHICLE_ID))
                .add(ReservationRestDtoMother.randomWithVehicleId(ANY_VEHICLE_ID))
                .build());
        String fetchVehiclesResponseJson = jsonMapper.writeValueAsString(VALID_VEHICLE_IDS.stream()
                .map(CarSharingVehicleRestDtoMother::randomWithVehicleId)
                .collect(toList()));
        String updateAlwaysPowerOnResponseJson = jsonMapper.writeValueAsString(CarSharingVehicleRestDtoMother.randomWithVehicleId(ANY_VEHICLE_ID));

        String queryString = VALID_VEHICLE_IDS.stream()
                .map(vehicleId -> String.format("vehicleId=%s", vehicleId))
                .collect(joining("&"));

        this.mockClient = new MockClient()
                .add(HttpMethod.POST, String.format("/api/rest/v1/car-sharing/vehicle/%d/reservation?contractId=%d", ANY_VEHICLE_ID, ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .body(createReservationResponseJson, Charsets.UTF_8)
                        .headers(Collections.emptyMap()))
                .add(HttpMethod.GET, String.format("/api/rest/v1/car-sharing/vehicle/%d/reservation?contractId=%d", ANY_VEHICLE_ID, ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .body(fetchReservationResponseJson, Charsets.UTF_8)
                        .headers(Collections.emptyMap()))
                .add(HttpMethod.DELETE, String.format("/api/rest/v1/car-sharing/vehicle/%d/reservation/%d?contractId=%d", ANY_VEHICLE_ID, ANY_RESERVATION_ID, ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .body("true", Charsets.UTF_8)
                        .headers(Collections.emptyMap()))
                .add(HttpMethod.POST, String.format("/api/rest/v1/car-sharing/vehicle/%d/always-power-on?contractId=%d", ANY_VEHICLE_ID, ANY_CONTRACT_ID), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .body(updateAlwaysPowerOnResponseJson, Charsets.UTF_8)
                        .headers(Collections.emptyMap()))
                .add(HttpMethod.GET, String.format("/api/rest/v1/car-sharing/vehicle?contractId=%d&%s", ANY_CONTRACT_ID, queryString), Response.builder()
                        .status(HttpStatus.OK.value())
                        .reason(HttpStatus.OK.getReasonPhrase())
                        .body(fetchVehiclesResponseJson, Charsets.UTF_8)
                        .headers(Collections.emptyMap()));


        Target<CarSharingReservationClient> mockTarget = new MockTarget<>(CarSharingReservationClient.class);

        ClientConfig<CarSharingReservationClient> config = ClientConfig.ConfigurableClientConfig.<CarSharingReservationClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        this.sut = TrafficsoftClients.carSharingReservation(config);
    }

    @Test
    public void itShouldCreateReservation() throws Exception {
        ReservationRestDto request = ReservationRestDtoMother.randomWithVehicleId(ANY_VEHICLE_ID);

        ReservationRestDto returnValue = this.sut.createReservation(ANY_CONTRACT_ID, ANY_VEHICLE_ID, request).execute();

        assertThat(returnValue, is(notNullValue()));
        assertThat(returnValue.getVehicleId(), is(ANY_VEHICLE_ID));
        assertThat(returnValue.getReservationId(), is(notNullValue()));
        assertThat(returnValue.getFrom(), is(notNullValue()));
        assertThat(returnValue.getUntil(), is(notNullValue()));
        assertThat(returnValue.getBtle(), is(notNullValue()));
        assertThat(returnValue.getBtle().getAppId(), is(notNullValue()));
        assertThat(returnValue.getBtle().getMobileSerialNumber(), is(notNullValue()));
        assertThat(returnValue.getRfid(), is(notNullValue()));
        assertThat(returnValue.getRfid().getDriverTagId(), is(notNullValue()));

        String url = String.format("/api/rest/v1/car-sharing/vehicle/%d/reservation?contractId=%d", ANY_VEHICLE_ID, ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.POST, url);
    }

    @Test
    public void itShouldCancelReservation() throws Exception {
        Boolean returnValue = this.sut.cancelReservation(ANY_CONTRACT_ID, ANY_VEHICLE_ID, ANY_RESERVATION_ID).execute();

        assertThat(returnValue, is(notNullValue()));

        String url = String.format("/api/rest/v1/car-sharing/vehicle/%d/reservation/%d?contractId=%d", ANY_VEHICLE_ID, ANY_RESERVATION_ID, ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.DELETE, url);
    }

    @Test
    public void itShouldFetchReservation() throws Exception {
        List<ReservationRestDto> returnValue = this.sut.fetchReservations(ANY_CONTRACT_ID, ANY_VEHICLE_ID).execute();

        assertThat(returnValue, is(notNullValue()));

        assertThat(returnValue, hasSize(greaterThanOrEqualTo(2)));
        assertThat(returnValue.get(0), is(notNullValue()));
        assertThat(returnValue.get(0).getVehicleId(), is(ANY_VEHICLE_ID));
        assertThat(returnValue.get(0).getReservationId(), is(notNullValue()));
        assertThat(returnValue.get(0).getFrom(), is(notNullValue()));
        assertThat(returnValue.get(0).getUntil(), is(notNullValue()));

        assertThat(returnValue.get(1), is(notNullValue()));
        assertThat(returnValue.get(1).getVehicleId(), is(ANY_VEHICLE_ID));
        assertThat(returnValue.get(1).getReservationId(), is(notNullValue()));
        assertThat(returnValue.get(1).getFrom(), is(notNullValue()));
        assertThat(returnValue.get(1).getUntil(), is(notNullValue()));

        String url = String.format("/api/rest/v1/car-sharing/vehicle/%d/reservation?contractId=%d", ANY_VEHICLE_ID, ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.GET, url);
    }

    @Test
    public void itShouldFetchVehicles() throws Exception {
        List<CarSharingVehicleRestDto> returnValue = this.sut.fetchVehicles(ANY_CONTRACT_ID, VALID_VEHICLE_IDS).execute();

        assertThat(returnValue, is(notNullValue()));

        checkArgument(VALID_VEHICLE_IDS.size() >= 2, "Sanity check");

        assertThat(returnValue, hasSize(VALID_VEHICLE_IDS.size()));
        assertThat(returnValue.get(0), is(notNullValue()));
        assertThat(returnValue.get(0).getVehicleId(), is(VALID_VEHICLE_IDS.get(0)));
        assertThat(returnValue.get(0).getReservationIds(), is(notNullValue()));

        assertThat(returnValue.get(1), is(notNullValue()));
        assertThat(returnValue.get(1).getVehicleId(), is(VALID_VEHICLE_IDS.get(1)));
        assertThat(returnValue.get(1).getReservationIds(), is(notNullValue()));

        String queryString = VALID_VEHICLE_IDS.stream()
                .map(vehicleId -> String.format("vehicleId=%s", vehicleId))
                .collect(joining("&"));
        String url = String.format("/api/rest/v1/car-sharing/vehicle?contractId=%d&%s", ANY_CONTRACT_ID, queryString);
        this.mockClient.verifyOne(HttpMethod.GET, url);
    }

    @Test
    public void itShouldUpdateVehicleAlwaysPowerOn() throws Exception {
        CarSharingVehicleRestDto returnValue = this.sut.updateVehicleAlwaysPowerOn(ANY_CONTRACT_ID, ANY_VEHICLE_ID, new VehiclePowerOnRequestRestDto(true)).execute();

        assertThat(returnValue, is(notNullValue()));

        String url = String.format("/api/rest/v1/car-sharing/vehicle/%d/always-power-on?contractId=%d", ANY_VEHICLE_ID, ANY_CONTRACT_ID);
        this.mockClient.verifyOne(HttpMethod.POST, url);
    }

}

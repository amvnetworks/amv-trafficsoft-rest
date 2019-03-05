package org.amv.trafficsoft.rest.client.carsharing.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.amv.trafficsoft.rest.carsharing.reservation.model.ReservationResponseRestDto;
import org.amv.trafficsoft.rest.carsharing.reservation.model.ReservationResponseRestDtoMother;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CarSharingReservationJsonTest {
    private final ObjectMapper jsonMapper = ClientConfig.ConfigurableClientConfig.defaultObjectMapper
            .setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Berlin")));

    @Test
    public void testToJson() throws JsonProcessingException {
        ReservationResponseRestDto reservationResponse = ReservationResponseRestDtoMother
                .randomBuilder()
                .from(Date.from(Instant.ofEpochSecond(1551793229)))
                .until(null)
                .build();

        String reservationResponseJson = jsonMapper
                .writeValueAsString(reservationResponse);

        DocumentContext documentContext = JsonPath.parse(reservationResponseJson);

        String from = documentContext.read("from", String.class);

        assertThat(from, equalTo("2019-03-05T14:40:29.000+01:00"));
    }

    @Test
    public void testFromJson() throws IOException, ParseException {
        String reservationResponseJson = "{\n" +
                "    \"vehicleId\" : 1,\n" +
                "    \"reservationId\" : 2,\n" +
                "    \"rfid\" : {\n" +
                "        \"driverTagId\" : \"35198184\"\n" +
                "    },\n" +
                "    \"btle\" : {\n" +
                "        \"appId\" : \"2VKtSiZGPARk\",\n" +
                "        \"mobileSerialNumber\" : \"05590524\",\n" +
                "        \"accessCertificateId\" : \"2c64458c-7a7b-4412-9a0a-8f960790001a\"\n" +
                "    },\n" +
                "    \"from\" : \"2018-01-30T23:34:49.181+09:00\"\n" +
                "}";

        ReservationResponseRestDto reservationResponse = jsonMapper
                .readerFor(ReservationResponseRestDto.class)
                .readValue(reservationResponseJson);

        assertThat(reservationResponse, is(notNullValue()));
        assertThat(reservationResponse.getVehicleId(), is(1L));
        assertThat(reservationResponse.getReservationId(), is(2L));
        assertThat(reservationResponse, is(notNullValue()));
        assertThat(reservationResponse.getFrom(), is(new ISO8601DateFormat()
                .parse("2018-01-30T17:04:49.181+02:30")
        ));
        assertThat(reservationResponse.getUntil(), is(nullValue()));

        assertThat(reservationResponse.getRfid().getDriverTagId(), is("35198184"));

        assertThat(reservationResponse.getBtle().getAppId(), is("2VKtSiZGPARk"));
        assertThat(reservationResponse.getBtle().getMobileSerialNumber(), is("05590524"));
        assertThat(reservationResponse.getBtle().getAccessCertificateId(),
                is("2c64458c-7a7b-4412-9a0a-8f960790001a"));
    }
}

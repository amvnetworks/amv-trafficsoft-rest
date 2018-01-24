package org.amv.trafficsoft.rest.client.carsharing.reservation;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.carsharing.reservation.model.CarSharingVehicleResponseRestDto;
import org.amv.trafficsoft.rest.carsharing.reservation.model.ReservationRequestRestDto;
import org.amv.trafficsoft.rest.carsharing.reservation.model.ReservationResponseRestDto;
import org.amv.trafficsoft.rest.carsharing.reservation.model.VehiclePowerOnRequestRestDto;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;

import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * A client for accessing the <i>car-sharing reservation</i> endpoint.
 */
public interface CarSharingReservationClient extends TrafficsoftClient {

    @RequestLine("GET /api/rest/v1/car-sharing/vehicle?contractId={contractId}&vehicleId={vehicleId}")
    HystrixCommand<List<CarSharingVehicleResponseRestDto>> fetchVehicles(
            @Param("contractId") long contractId,
            @Param("vehicleId") List<Long> vehicleIds);

    @RequestLine("GET /api/rest/v1/car-sharing/vehicle/{vehicleId}/reservation?contractId={contractId}")
    HystrixCommand<List<ReservationResponseRestDto>> fetchReservations(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId);

    @RequestLine("GET /api/rest/v1/car-sharing/vehicle/{vehicleId}/reservation?contractId={contractId}&reservationId={reservationId}")
    HystrixCommand<List<ReservationResponseRestDto>> fetchReservations(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId,
            @Param("reservationId") List<Long> reservationIds);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /api/rest/v1/car-sharing/vehicle/{vehicleId}/reservation?contractId={contractId}")
    HystrixCommand<ReservationResponseRestDto> createReservation(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId,
            ReservationRequestRestDto request);

    @RequestLine("DELETE /api/rest/v1/car-sharing/vehicle/{vehicleId}/reservation/{reservationId}?contractId={contractId}")
    HystrixCommand<Boolean> cancelReservation(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId,
            @Param("reservationId") long reservationId);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /api/rest/v1/car-sharing/vehicle/{vehicleId}/always-power-on?contractId={contractId}")
    HystrixCommand<CarSharingVehicleResponseRestDto> updateVehicleAlwaysPowerOn(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId,
            VehiclePowerOnRequestRestDto request);
}

package org.amv.trafficsoft.rest.client.carsharing.reservation;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.carsharing.reservation.model.CarSharingVehicleRestDto;
import org.amv.trafficsoft.rest.carsharing.reservation.model.ReservationRestDto;
import org.amv.trafficsoft.rest.carsharing.reservation.model.VehiclePowerOnRequestRestDto;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;

import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * A client for accessing the <i>car-sharing reservation</i> endpoint.
 */
public interface CarSharingReservationClient extends TrafficsoftClient {

    @RequestLine("GET /api/rest/v1/car-sharing/vehicle?contractId={contractId}&vehicleId={vehicleId}")
    HystrixCommand<List<CarSharingVehicleRestDto>> fetchVehicles(
            @Param("contractId") long contractId,
            @Param("vehicleId") List<Long> vehicleIds);

    @RequestLine("GET /api/rest/v1/car-sharing/vehicle/{vehicleId}/reservation?contractId={contractId}")
    HystrixCommand<List<ReservationRestDto>> fetchReservations(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /api/rest/v1/car-sharing/vehicle/{vehicleId}/reservation?contractId={contractId}")
    HystrixCommand<ReservationRestDto> createReservation(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId,
            ReservationRestDto request);

    @RequestLine("DELETE /api/rest/v1/car-sharing/vehicle/{vehicleId}/reservation/{reservationId}?contractId={contractId}")
    HystrixCommand<Boolean> cancelReservation(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId,
            @Param("reservationId") long reservationId);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /api/rest/v1/car-sharing/vehicle/{vehicleId}/always-power-on?contractId={contractId}")
    HystrixCommand<CarSharingVehicleRestDto> updateVehicleAlwaysPowerOn(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId,
            VehiclePowerOnRequestRestDto request);
}

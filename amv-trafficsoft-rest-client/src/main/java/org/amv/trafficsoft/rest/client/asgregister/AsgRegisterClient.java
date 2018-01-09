package org.amv.trafficsoft.rest.client.asgregister;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.asgregister.model.*;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * A client for accessing the <i>asg-register</i> endpoint.
 */
public interface AsgRegisterClient extends TrafficsoftClient {

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    //@RequestLine("POST /api/rest/v1/asg-register?contractId={contractId}")
    @RequestLine("POST /{contractId}/asg-register")
    HystrixCommand<RegisterAsgResponseRestDto> registerAsg(
            @Param("contractId") long contractId,
            RegisterAsgRequestRestDto request);

    //@RequestLine("GET /api/rest/v1/asg-register/vehiclekey/{vehicleKey}?contractId={contractId}")
    @RequestLine("GET /{contractId}/asg-register/vehiclekey/{vehicleKey}")
    HystrixCommand<VehicleKeyResponseRestDto> getVehicleKey(
            @Param("contractId") long contractId,
            @Param("vehicleKey") String vehicleKey);

    //@RequestLine("GET /api/rest/v1/asg-register/vehicle/{vehicleId}?contractId={contractId}")
    @RequestLine("GET /{contractId}/asg-register/vehicle/{vehicleId}")
    HystrixCommand<VehicleResponseRestDto> getVehicle(
            @Param("contractId") long contractId,
            @Param("vehicleId") long vehicleId);

    //@RequestLine("GET /api/rest/v1/asg-register/oem?contractId={contractId}")
    @RequestLine("GET /{contractId}/asg-register/oem")
    HystrixCommand<OemsResponseRestDto> getOems(@Param("contractId") long contractId);

    //@RequestLine("GET /api/rest/v1/asg-register/oem/{oemCode}/series?contractId={contractId}")
    @RequestLine("GET /{contractId}/asg-register/oem/{oemCode}/series")
    HystrixCommand<SeriesResponseRestDto> getSeries(
            @Param("contractId") long contractId,
            @Param("oemCode") String oemCode);

    //@RequestLine("GET /api/rest/v1/asg-register/oem/{oemCode}/series/{seriesCode}/model?contractId={contractId}")
    @RequestLine("GET /{contractId}/asg-register/oem/{oemCode}/series/{seriesCode}/model")
    HystrixCommand<ModelsResponseRestDto> getModels(
            @Param("contractId") long contractId,
            @Param("oemCode") String oemCode,
            @Param("seriesCode") String seriesCode);
}

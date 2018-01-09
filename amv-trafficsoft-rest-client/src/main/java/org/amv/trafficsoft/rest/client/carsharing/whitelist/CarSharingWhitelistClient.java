package org.amv.trafficsoft.rest.client.carsharing.whitelist;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.FetchWhitelistsResponseRestDto;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.UpdateWhitelistsRequestRestDto;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;

import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * A client for accessing the <i>car-sharing whitelist</i> endpoint.
 */
public interface CarSharingWhitelistClient extends TrafficsoftClient {

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    //@RequestLine("POST /api/rest/v1/car-sharing/whitelist?contractId={contractId}")
    @RequestLine("POST /{contractId}/car-sharing/whitelist")
    HystrixCommand<Void> updateWhitelists(
            @Param("contractId") long contractId,
            UpdateWhitelistsRequestRestDto request);

    //@RequestLine("GET /api/rest/v1/car-sharing/whitelist?vehicleId={vehicleId}&contractId={contractId}")
    @RequestLine("GET /{contractId}/car-sharing/whitelist?vehicleId={vehicleId}")
    HystrixCommand<FetchWhitelistsResponseRestDto> fetchWhitelists(
            @Param("contractId") long contractId,
            @Param("vehicleId") List<Long> vehicleIds);
}

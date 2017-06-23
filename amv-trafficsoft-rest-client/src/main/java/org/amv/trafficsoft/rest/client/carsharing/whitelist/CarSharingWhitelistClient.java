package org.amv.trafficsoft.rest.client.carsharing.whitelist;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.RetrieveWhitelistResponseRestDto;
import org.amv.trafficsoft.rest.carsharing.whitelist.model.UpdateWhitelistRequestRestDto;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;

import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * A client for accessing the <i>car-sharing whitelist</i> endpoint.
 *
 * @author Alois Leitner
 */
public interface CarSharingWhitelistClient extends TrafficsoftClient {

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /{contractId}/car-sharing/whitelist")
    HystrixCommand<Void> updateWhitelist(
            @Param("contractId") long contractId,
            UpdateWhitelistRequestRestDto request);

    @RequestLine("GET /{contractId}/car-sharing/whitelist?vehicleId={vehicleId}")
    HystrixCommand<RetrieveWhitelistResponseRestDto> retrieveWhitelist(
            @Param("contractId") long contractId,
            @Param("vehicleId") List<Long> vehicleIds);
}

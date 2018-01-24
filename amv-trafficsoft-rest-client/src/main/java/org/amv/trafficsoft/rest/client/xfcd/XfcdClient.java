package org.amv.trafficsoft.rest.client.xfcd;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;
import org.amv.trafficsoft.rest.xfcd.model.DeliveryRestDto;
import org.amv.trafficsoft.rest.xfcd.model.NodeRestDto;

import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * A client for accessing the <i>xfcd</i> endpoint.
 */
public interface XfcdClient extends TrafficsoftClient {

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /api/rest/v1/xfcd?contractId={contractId}")
    HystrixCommand<List<DeliveryRestDto>> getDataAndConfirmDeliveries(
            @Param("contractId") long contractId,
            List<Long> deliveryIds);

    @RequestLine("GET /api/rest/v1/xfcd?contractId={contractId}")
    HystrixCommand<List<DeliveryRestDto>> getData(
            @Param("contractId") long contractId);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /api/rest/v1/xfcd/confirm?contractId={contractId}")
    HystrixCommand<Void> confirmDeliveries(
            @Param("contractId") long contractId,
            List<Long> deliveryIds);

    @RequestLine("GET /api/rest/v1/xfcd/last?contractId={contractId}&vehicleId={vehicleId}")
    HystrixCommand<List<NodeRestDto>> getLatestData(
            @Param("contractId") long contractId,
            @Param(value = "vehicleId") List<Long> vehicleIds);
}

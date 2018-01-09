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
 *
 * @author Alois Leitner
 */
public interface XfcdClient extends TrafficsoftClient {

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /{contractId}/xfcd")
    HystrixCommand<List<DeliveryRestDto>> getDataAndConfirmDeliveries(
            @Param("contractId") long contractId,
            List<Long> deliveryIds);

    @RequestLine("GET /{contractId}/xfcd")
    HystrixCommand<List<DeliveryRestDto>> getData(
            @Param("contractId") long contractId);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /{contractId}/xfcd/confirm")
    HystrixCommand<Void> confirmDeliveries(
            @Param("contractId") long contractId,
            List<Long> deliveryIds);

    /**
     * @deprecated Please use {{@link #getLatestData(long, List)}} instead.
     */
    @Deprecated
    default HystrixCommand<List<NodeRestDto>> getLastData(
            long contractId,
            List<Long> vehicleIds) {
        return getLatestData(contractId, vehicleIds);
    }

    @RequestLine("GET /{contractId}/xfcd/last?vehicleId={vehicleId}")
    HystrixCommand<List<NodeRestDto>> getLatestData(
            @Param("contractId") long contractId,
            @Param(value = "vehicleId") List<Long> vehicleIds);
}

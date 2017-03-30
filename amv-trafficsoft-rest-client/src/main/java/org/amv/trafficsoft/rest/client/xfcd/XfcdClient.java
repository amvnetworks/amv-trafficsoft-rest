package org.amv.trafficsoft.rest.client.xfcd;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;
import org.amv.trafficsoft.rest.xfcd.model.*;

import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

public interface XfcdClient extends TrafficsoftClient {

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /{contractId}/xfcd")
    HystrixCommand<List<DeliveryDto>> getDataAndConfirmDeliveries(
            @Param("contractId") long contractId,
            List<Long> deliveryIds);

    @RequestLine("GET /{contractId}/xfcd")
    HystrixCommand<List<DeliveryDto>> getData(
            @Param("contractId") long contractId);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /{contractId}/xfcd")
    HystrixCommand<List<DeliveryDto>> confirmDeliveries(
            @Param("contractId") long contractId,
            List<Long> deliveryIds);

    @Headers({
            CONTENT_TYPE + ": " + "application/json;charset=UTF-8"
    })
    @RequestLine("POST /{contractId}/xfcd/last")
    HystrixCommand<List<NodeDto>> getLastData(
            @Param("contractId") long contractId,
            List<Long> vehicleIds);
}
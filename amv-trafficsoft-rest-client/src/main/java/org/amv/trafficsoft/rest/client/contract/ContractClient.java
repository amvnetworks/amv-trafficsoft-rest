package org.amv.trafficsoft.rest.client.contract;

import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.amv.trafficsoft.rest.client.TrafficsoftClient;
import org.amv.trafficsoft.rest.contract.datapackage.model.DataPackageResponseRestDto;
import org.amv.trafficsoft.rest.contract.subscription.model.SubscriptionsResponseRestDto;

import static com.google.common.net.HttpHeaders.ACCEPT_LANGUAGE;

public interface ContractClient extends TrafficsoftClient {

    default HystrixCommand<DataPackageResponseRestDto> fetchDataPackage(long contractId) {
        return fetchDataPackage(contractId, "en");
    }

    @Headers({
            ACCEPT_LANGUAGE + ": " + "{language}"
    })
    @RequestLine("GET /api/rest/v1/contract/{contractId}/datapackage?contractId={contractId}")
    HystrixCommand<DataPackageResponseRestDto> fetchDataPackage(
            @Param("contractId") long contractId,
            @Param("language") String language);
    
    @RequestLine("GET /api/rest/v1/contract/{contractId}/subscription?contractId={contractId}")
    HystrixCommand<SubscriptionsResponseRestDto> fetchSubscriptions(
            @Param("contractId") long contractId);
}

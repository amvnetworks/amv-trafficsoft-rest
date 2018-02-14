package org.amv.trafficsoft.rest.client;

import feign.Headers;

import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.USER_AGENT;

/**
 * Base class for all TrafficSoft clients.
 */
@Headers({
        ACCEPT + ": " + "application/json;charset=UTF-8",
        USER_AGENT + ": amv-rest-client v0.0.1-SNAPSHOT"
})
public interface TrafficsoftClient {

}

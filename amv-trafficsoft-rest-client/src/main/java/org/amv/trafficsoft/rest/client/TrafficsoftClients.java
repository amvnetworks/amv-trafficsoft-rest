package org.amv.trafficsoft.rest.client;

import feign.Target;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;

import static java.util.Objects.requireNonNull;

public final class TrafficsoftClients {

    private TrafficsoftClients() {
        throw new UnsupportedOperationException();
    }

    public static <T> ClientConfig<T> config(Class<T> clazz, String baseUrl, ClientConfig.BasicAuth basicAuth) {
        requireNonNull(clazz, "`clazz` must not be null.");
        requireNonNull(baseUrl, "`baseUrl` must not be null.");

        Target<T> hardCodedTarget = new Target.HardCodedTarget<>(clazz, baseUrl);

        return ClientConfig.ConfigurableClientConfig.<T>builder()
                .target(hardCodedTarget)
                .basicAuth(basicAuth)
                .build();
    }

    public static AsgRegisterClient asgRegister(ClientConfig<AsgRegisterClient> clientConfig) {
        return Clients.create(clientConfig);
    }

    public static XfcdClient xfcd(ClientConfig<XfcdClient> clientConfig) {
        return Clients.create(clientConfig);
    }
}

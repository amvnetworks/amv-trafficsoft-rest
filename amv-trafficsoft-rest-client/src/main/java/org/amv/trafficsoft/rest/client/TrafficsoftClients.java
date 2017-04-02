package org.amv.trafficsoft.rest.client;

import feign.Target;
import org.amv.trafficsoft.rest.client.ClientConfig.ConfigurableClientConfig;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;

import static java.util.Objects.requireNonNull;

public final class TrafficsoftClients {

    private TrafficsoftClients() {
        throw new UnsupportedOperationException();
    }

    public static <T> ConfigurableClientConfig.Builder<T> config(Class<T> clazz, String baseUrl, ClientConfig.BasicAuth basicAuth) {
        requireNonNull(clazz, "`clazz` must not be null.");
        requireNonNull(baseUrl, "`baseUrl` must not be null.");
        requireNonNull(basicAuth, "`basicAuth` must not be null.");

        Target<T> hardCodedTarget = new Target.HardCodedTarget<>(clazz, baseUrl);

        return ConfigurableClientConfig.<T>builder()
                .target(hardCodedTarget)
                .basicAuth(basicAuth);
    }

    public static AsgRegisterClient asgRegister(String baseUrl, ClientConfig.BasicAuth basicAuth) {
        return asgRegister(config(AsgRegisterClient.class, baseUrl, basicAuth).build());
    }

    public static AsgRegisterClient asgRegister(ClientConfig<AsgRegisterClient> clientConfig) {
        return Clients.create(clientConfig);
    }

    public static XfcdClient xfcd(String baseUrl, ClientConfig.BasicAuth basicAuth) {
        return xfcd(config(XfcdClient.class, baseUrl, basicAuth).build());
    }

    public static XfcdClient xfcd(ClientConfig<XfcdClient> clientConfig) {
        return Clients.create(clientConfig);
    }
}

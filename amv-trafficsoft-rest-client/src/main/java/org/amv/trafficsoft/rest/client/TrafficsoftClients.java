package org.amv.trafficsoft.rest.client;

import feign.Target;
import org.amv.trafficsoft.rest.client.ClientConfig.ConfigurableClientConfig;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.carsharing.reservation.CarSharingReservationClient;
import org.amv.trafficsoft.rest.client.carsharing.whitelist.CarSharingWhitelistClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;

import static java.util.Objects.requireNonNull;

/**
 * A factory class for creating client instances.
 * <p>
 * This class exists for simpler construction of new clients
 * for the various services to access different parts of the API.
 *
 * @author Alois Leitner
 */
public final class TrafficsoftClients {

    private TrafficsoftClients() {
        throw new UnsupportedOperationException();
    }

    /**
     * Provides a way to easily construct configuration builder objects.
     *
     * @param clazz     the client class the returned configuration object is for
     * @param baseUrl   the base url of the api
     * @param basicAuth the authorisation object for accessing the api
     * @param <T>       the type of the client class the returned configuration object is for
     * @return a builder for easy construction of custom configuration
     */
    public static <T> ConfigurableClientConfig.Builder<T> config(Class<T> clazz, String baseUrl, ClientConfig.BasicAuth basicAuth) {
        requireNonNull(clazz, "`clazz` must not be null.");
        requireNonNull(baseUrl, "`baseUrl` must not be null.");
        requireNonNull(basicAuth, "`basicAuth` must not be null.");

        Target<T> hardCodedTarget = new Target.HardCodedTarget<>(clazz, baseUrl);

        return ConfigurableClientConfig.<T>builder()
                .target(hardCodedTarget)
                .basicAuth(basicAuth);
    }

    /**
     * Constructs a new client from a generic client configuration.
     *
     * @param clientConfig a configuration instance to configure the client
     * @param <T>          the class of the client the given configuration is configuring
     * @return a client configured with the given configuration
     * @apiNote This method is currently `private` because there are very few services available.
     * Consider making this method publicly available if amount of services grows.
     */
    private static <T> T client(ClientConfig<T> clientConfig) {
        return Clients.create(clientConfig);
    }

    /**
     * Constructs a new AsgRegisterClient with default configuration.
     *
     * @param baseUrl   the base url of the api
     * @param basicAuth the authorisation object for accessing the api
     * @return a AsgRegisterClient with default configuration
     */
    public static AsgRegisterClient asgRegister(String baseUrl, ClientConfig.BasicAuth basicAuth) {
        return asgRegister(config(AsgRegisterClient.class, baseUrl, basicAuth).build());
    }

    /**
     * Constructs a new AsgRegisterClient with custom configuration.
     *
     * @param clientConfig the configuration to use
     * @return a AsgRegisterClient configured with the given configuration
     */
    public static AsgRegisterClient asgRegister(ClientConfig<AsgRegisterClient> clientConfig) {
        return client(clientConfig);
    }

    /**
     * Constructs a new XfcdClient with default configuration.
     *
     * @param baseUrl   the base url of the api
     * @param basicAuth the authorisation object for accessing the api
     * @return a XfcdClient with default configuration
     */
    public static XfcdClient xfcd(String baseUrl, ClientConfig.BasicAuth basicAuth) {
        return xfcd(config(XfcdClient.class, baseUrl, basicAuth).build());
    }

    /**
     * Constructs a new XfcdClient with custom configuration.
     *
     * @param clientConfig the configuration to use
     * @return a XfcdClient configured with the given configuration
     */
    public static XfcdClient xfcd(ClientConfig<XfcdClient> clientConfig) {
        return client(clientConfig);
    }

    /**
     * Constructs a new CarSharingWhitelistClient with default configuration.
     *
     * @param baseUrl   the base url of the api
     * @param basicAuth the authorisation object for accessing the api
     * @return a CarSharingWhitelistClient with default configuration
     */
    public static CarSharingWhitelistClient carSharingWhitelist(String baseUrl, ClientConfig.BasicAuth basicAuth) {
        return carSharingWhitelist(config(CarSharingWhitelistClient.class, baseUrl, basicAuth).build());
    }

    /**
     * Constructs a new CarSharingWhitelistClient with custom configuration.
     *
     * @param clientConfig the configuration to use
     * @return a CarSharingWhitelistClient configured with the given configuration
     */
    public static CarSharingWhitelistClient carSharingWhitelist(ClientConfig<CarSharingWhitelistClient> clientConfig) {
        return client(clientConfig);
    }

    /**
     * Constructs a new CarSharingReservationClient with default configuration.
     *
     * @param baseUrl   the base url of the api
     * @param basicAuth the authorisation object for accessing the api
     * @return a CarSharingReservationClient with default configuration
     */
    public static CarSharingReservationClient carSharingReservation(String baseUrl, ClientConfig.BasicAuth basicAuth) {
        return carSharingReservation(config(CarSharingReservationClient.class, baseUrl, basicAuth).build());
    }

    /**
     * Constructs a new CarSharingReservationClient with custom configuration.
     *
     * @param clientConfig the configuration to use
     * @return a CarSharingReservationClient configured with the given configuration
     */
    public static CarSharingReservationClient carSharingReservation(ClientConfig<CarSharingReservationClient> clientConfig) {
        return client(clientConfig);
    }
}

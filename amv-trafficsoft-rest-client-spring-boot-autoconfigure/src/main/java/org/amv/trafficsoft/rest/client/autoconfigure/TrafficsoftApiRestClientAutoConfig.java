package org.amv.trafficsoft.rest.client.autoconfigure;

import com.netflix.hystrix.*;
import feign.Feign;
import feign.hystrix.SetterFactory;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.ClientConfig.ConfigurableClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.carsharing.reservation.CarSharingReservationClient;
import org.amv.trafficsoft.rest.client.carsharing.whitelist.CarSharingWhitelistClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@ConditionalOnProperty("amv.trafficsoft.api.rest.baseUrl")
@EnableConfigurationProperties(TrafficsoftApiRestProperties.class)
public class TrafficsoftApiRestClientAutoConfig {

    private final TrafficsoftApiRestProperties trafficsoftApiRestProperties;

    @Autowired
    public TrafficsoftApiRestClientAutoConfig(TrafficsoftApiRestProperties trafficsoftApiRestProperties) {
        this.trafficsoftApiRestProperties = requireNonNull(trafficsoftApiRestProperties);
    }

    @ConditionalOnMissingBean
    @Bean("trafficsoftApiRestClientBasicAuth")
    public ClientConfig.BasicAuth basicAuth() {
        return ClientConfig.BasicAuthImpl.builder()
                .username(trafficsoftApiRestProperties.getUsername())
                .password(trafficsoftApiRestProperties.getPassword())
                .build();
    }

    @ConditionalOnMissingBean(name = "trafficsoftApiRestXfcdClientConfig")
    @Bean("trafficsoftApiRestXfcdClientConfig")
    public ConfigurableClientConfig<XfcdClient> xfcdClientConfig(ClientConfig.BasicAuth basicAuth) {
        return TrafficsoftClients.config(XfcdClient.class, this.trafficsoftApiRestProperties.getBaseUrl(), basicAuth)
                .setterFactory(setterFactory())
                .build();
    }

    @ConditionalOnMissingBean
    @Bean("trafficsoftApiRestXfcdClient")
    public XfcdClient xfcdClient(ConfigurableClientConfig<XfcdClient> xfcdClientConfig) {
        return TrafficsoftClients.xfcd(xfcdClientConfig);
    }

    @ConditionalOnMissingBean(name = "trafficsoftApiRestCarSharingWhitelistClientConfig")
    @Bean("trafficsoftApiRestCarSharingWhitelistClientConfig")
    public ConfigurableClientConfig<CarSharingWhitelistClient> carSharingWhitelistConfig(ClientConfig.BasicAuth basicAuth) {
        return TrafficsoftClients.config(CarSharingWhitelistClient.class, this.trafficsoftApiRestProperties.getBaseUrl(), basicAuth)
                .setterFactory(setterFactory())
                .build();
    }

    @ConditionalOnMissingBean
    @Bean("trafficsoftApiRestCarSharingWhitelistClient")
    public CarSharingWhitelistClient carSharingWhitelistClient(ConfigurableClientConfig<CarSharingWhitelistClient> carSharingWhitelistConfig) {
        return TrafficsoftClients.carSharingWhitelist(carSharingWhitelistConfig);
    }

    @ConditionalOnMissingBean(name = "trafficsoftApiRestCarSharingReservationClientConfig")
    @Bean("trafficsoftApiRestCarSharingReservationClientConfig")
    public ConfigurableClientConfig<CarSharingReservationClient> carSharingReservationConfig(ClientConfig.BasicAuth basicAuth) {
        return TrafficsoftClients.config(CarSharingReservationClient.class, this.trafficsoftApiRestProperties.getBaseUrl(), basicAuth)
                .setterFactory(setterFactory())
                .build();
    }

    @ConditionalOnMissingBean
    @Bean("trafficsoftApiRestCarSharingReservationClient")
    public CarSharingReservationClient carSharingReservationClient(ConfigurableClientConfig<CarSharingReservationClient> carSharingReservationConfig) {
        return TrafficsoftClients.carSharingReservation(carSharingReservationConfig);
    }

    @ConditionalOnMissingBean(name = "trafficsoftApiRestAsgRegisterClientConfig")
    @Bean("trafficsoftApiRestAsgRegisterClientConfig")
    public ConfigurableClientConfig<AsgRegisterClient> asgRegisterClientConfig(ClientConfig.BasicAuth basicAuth) {
        return TrafficsoftClients.config(AsgRegisterClient.class, this.trafficsoftApiRestProperties.getBaseUrl(), basicAuth, TrafficsoftClients.getListRequestInterceptor())
                .setterFactory(setterFactory())
                .build();
    }

    @ConditionalOnMissingBean
    @Bean("trafficsoftApiRestAsgRegisterClient")
    public AsgRegisterClient asgRegisterClient(ConfigurableClientConfig<AsgRegisterClient> asgRegisterClientConfig) {
        return TrafficsoftClients.asgRegister(asgRegisterClientConfig);
    }

    @ConditionalOnMissingBean(name = "trafficsoftApiRestClientSetterFactory")
    @Bean("trafficsoftApiRestClientSetterFactory")
    public SetterFactory setterFactory() {
        return (target, method) -> {
            String groupKey = target.name();
            String commandKey = Feign.configKey(target.type(), method);

            HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                    .withCoreSize(1);

            HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                    .withRequestLogEnabled(true)
                    .withFallbackEnabled(true)
                    .withExecutionTimeoutEnabled(true)
                    .withExecutionTimeoutInMilliseconds((int) SECONDS.toMillis(45))
                    .withExecutionIsolationStrategy(SEMAPHORE)
                    .withExecutionIsolationSemaphoreMaxConcurrentRequests(20);

            return HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                    .andThreadPoolPropertiesDefaults(threadPoolProperties)
                    .andCommandPropertiesDefaults(commandProperties);
        };
    }

}

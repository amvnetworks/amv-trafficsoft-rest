package org.amv.trafficsoft.rest.client.autoconfigure;

import com.netflix.hystrix.*;
import feign.Feign;
import feign.hystrix.SetterFactory;
import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.ClientConfig.ConfigurableClientConfig;
import org.amv.trafficsoft.rest.client.TrafficsoftClients;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.carsharing.whitelist.CarSharingWhitelistClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Bean("trafficsoftApiRestClientBasicAuth")
    public ClientConfig.BasicAuth basicAuth() {
        return ClientConfig.BasicAuthImpl.builder()
                .username(trafficsoftApiRestProperties.getUsername())
                .password(trafficsoftApiRestProperties.getPassword())
                .build();
    }

    @Bean("trafficsoftApiRestXfcdClientConfig")
    public ConfigurableClientConfig<XfcdClient> xfcdClientConfig() {
        return TrafficsoftClients.config(XfcdClient.class, this.trafficsoftApiRestProperties.getBaseUrl(), basicAuth())
                .setterFactory(setterFactory())
                .build();
    }

    @Bean("trafficsoftApiRestXfcdClient")
    public XfcdClient xfcdClient() {
        return TrafficsoftClients.xfcd(xfcdClientConfig());
    }

    @Bean("trafficsoftApiRestCarSharingWhitelistClientConfig")
    public ConfigurableClientConfig<CarSharingWhitelistClient> carSharingWhitelistConfig() {
        return TrafficsoftClients.config(CarSharingWhitelistClient.class, this.trafficsoftApiRestProperties.getBaseUrl(), basicAuth())
                .setterFactory(setterFactory())
                .build();
    }

    @Bean("trafficsoftApiRestCarSharingWhitelistClient")
    public CarSharingWhitelistClient carSharingWhitelistClient() {
        return TrafficsoftClients.carSharingWhitelist(carSharingWhitelistConfig());
    }

    @Bean("trafficsoftApiRestAsgRegisterClientConfig")
    public ConfigurableClientConfig<AsgRegisterClient> asgRegisterClientConfig() {
        return TrafficsoftClients.config(AsgRegisterClient.class, this.trafficsoftApiRestProperties.getBaseUrl(), basicAuth())
                .setterFactory(setterFactory())
                .build();
    }

    @Bean("trafficsoftApiRestAsgRegisterClient")
    public AsgRegisterClient asgRegisterClient() {
        return TrafficsoftClients.asgRegister(asgRegisterClientConfig());
    }

    @Bean("trafficsoftApiRestClientSetterFactory")
    public SetterFactory setterFactory() {
        return (target, method) -> {
            String groupKey = target.name();
            String commandKey = Feign.configKey(target.type(), method);

            HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                    .withCoreSize(1);

            HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                    .withRequestLogEnabled(true)
                    .withFallbackEnabled(false)
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
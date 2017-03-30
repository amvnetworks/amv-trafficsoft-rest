package org.amv.trafficsoft.rest.client;

import feign.Target;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.amv.trafficsoft.rest.client.ClientConfig.ConfigurableClientConfig;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientDemoTestConfig {

    @Bean
    public AsgRegisterClient asgRegisterClient() {
        MockClient mockClient = new MockClient();
        Target<AsgRegisterClient> mockTarget = new MockTarget<>(AsgRegisterClient.class);

        ClientConfig<AsgRegisterClient> config = ConfigurableClientConfig.<AsgRegisterClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        return TrafficsoftClients.asgRegister(config);
    }

    @Bean
    public XfcdClient xfcdClient() {
        MockClient mockClient = new MockClient();
        Target<XfcdClient> mockTarget = new MockTarget<>(XfcdClient.class);

        ClientConfig<XfcdClient> config = ConfigurableClientConfig.<XfcdClient>builder()
                .client(mockClient)
                .target(mockTarget)
                .build();

        return TrafficsoftClients.xfcd(config);
    }

}

package org.amv.trafficsoft.rest.client;

import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientDemoTestConfig {

    @Bean
    public ClientConfig.BasicAuth basicAuth() {
        return new ClientConfig.BasicAuth() {
            @Override
            public String username() {
                return RandomStringUtils.randomAscii(10);
            }

            @Override
            public String password() {
                return RandomStringUtils.randomAscii(120);
            }
        };
    }

    @Bean
    public ClientConfig<AsgRegisterClient> asgRegisterClientConfig() {
        return TrafficsoftClients.config(AsgRegisterClient.class, "https://example.com", basicAuth());
    }

    @Bean
    public AsgRegisterClient asgRegisterClient() {
        return TrafficsoftClients.asgRegister(asgRegisterClientConfig());
    }

    @Bean
    public ClientConfig<XfcdClient> xfcdClientClientConfig() {
        return TrafficsoftClients.config(XfcdClient.class, "https://example.com", basicAuth());
    }

    @Bean
    public XfcdClient xfcdClient() {
        return TrafficsoftClients.xfcd(xfcdClientClientConfig());
    }
}

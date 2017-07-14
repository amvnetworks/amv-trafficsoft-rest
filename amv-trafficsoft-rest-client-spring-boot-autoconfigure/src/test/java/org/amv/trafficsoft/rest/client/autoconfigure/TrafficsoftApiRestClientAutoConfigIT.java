package org.amv.trafficsoft.rest.client.autoconfigure;

import org.amv.trafficsoft.rest.client.ClientConfig;
import org.amv.trafficsoft.rest.client.ClientConfig.BasicAuth;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.carsharing.whitelist.CarSharingWhitelistClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrafficsoftApiRestClientAutoConfigIT.TestApplication.class)
public class TrafficsoftApiRestClientAutoConfigIT {

    @SpringBootApplication
    public static class TestApplication {

    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        final TrafficsoftApiRestClientAutoConfig restClientAutoConfig = applicationContext
                .getBean(TrafficsoftApiRestClientAutoConfig.class);

        assertThat(restClientAutoConfig, is(notNullValue()));
    }

    @Test
    public void createsBeanOfTypeBasicAuth() {
        final BasicAuth basicAuth = applicationContext.getBean(BasicAuth.class);

        assertThat(basicAuth, is(notNullValue()));
    }

    @Test
    public void createsBeansOfTypeClientConfig() {
        final Map<String, ClientConfig> beansOfTypeClientConfig = applicationContext
                .getBeansOfType(ClientConfig.class);

        assertThat(beansOfTypeClientConfig, is(notNullValue()));
        assertThat(beansOfTypeClientConfig.keySet(), hasSize(3));
    }

    @Test
    public void createsBeanOfTypeAsgRegisterClient() {
        final AsgRegisterClient asgRegisterClient = applicationContext.getBean(AsgRegisterClient.class);

        assertThat(asgRegisterClient, is(notNullValue()));
    }

    @Test
    public void createsBeanOfTypeCarSharingWhitelistClient() {
        final CarSharingWhitelistClient carSharingWhitelistClient = applicationContext
                .getBean(CarSharingWhitelistClient.class);

        assertThat(carSharingWhitelistClient, is(notNullValue()));
    }

    @Test
    public void createsBeanOfTypeXfcdClientClient() {
        final XfcdClient xfcdClient = applicationContext.getBean(XfcdClient.class);
        assertThat(xfcdClient, is(notNullValue()));
    }

}

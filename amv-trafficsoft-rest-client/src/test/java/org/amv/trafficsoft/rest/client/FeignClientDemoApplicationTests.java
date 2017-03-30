package org.amv.trafficsoft.rest.client;

import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.amv.trafficsoft.rest.client.xfcd.XfcdClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FeignClientDemoApplicationTests {

    @Autowired
    private Optional<AsgRegisterClient> asgRegisterClient = Optional.empty();

    @Autowired
    private Optional<XfcdClient> xfcdClient = Optional.empty();

    @Test
    public void contextLoads() {
        assertThat(asgRegisterClient.isPresent(), is(true));
        assertThat(xfcdClient.isPresent(), is(true));
    }

}

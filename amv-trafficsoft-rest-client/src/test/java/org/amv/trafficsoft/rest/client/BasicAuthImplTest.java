package org.amv.trafficsoft.rest.client;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class BasicAuthImplTest {

    @Test
    public void itShouldBePossibleToConstructABasicAuthObjectEasily() throws Exception {
        String username = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);
        
        ClientConfig.BasicAuth basicAuth = ClientConfig.BasicAuthImpl.builder()
                .username(username)
                .password(password)
                .build();

        assertThat(basicAuth, is(notNullValue()));
        assertThat(basicAuth.username(), is(equalTo(username)));
        assertThat(basicAuth.password(), is(equalTo(password)));
    }

}

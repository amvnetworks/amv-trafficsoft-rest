package org.amv.trafficsoft.rest.client;

import com.google.common.collect.ImmutableMap;
import com.netflix.hystrix.*;
import feign.*;
import feign.hystrix.SetterFactory;
import org.amv.trafficsoft.rest.client.ClientConfig.ConfigurableClientConfig;
import org.amv.trafficsoft.rest.client.asgregister.AsgRegisterClient;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ConfigurableClientConfigTest {

    @Test(expected = NullPointerException.class)
    public void itShouldNotBePossibleToCreateConfigWithoutTarget() {
        ConfigurableClientConfig.builder().build();
        Assert.fail("Should have thrown NullPointerException.");
    }

    @Test
    public void itShouldBePossibleToCreateADefaultConfig() {
        ConfigurableClientConfig<AsgRegisterClient> defaultConfig = ConfigurableClientConfig.<AsgRegisterClient>builder()
                .target(new Target.HardCodedTarget<>(AsgRegisterClient.class, "https://www.example.com"))
                .build();

        assertThat(defaultConfig, is(notNullValue()));
        assertThat(defaultConfig.target(), is(notNullValue()));
        assertThat(defaultConfig.basicAuth(), is(Optional.empty()));
        assertThat(defaultConfig.basicAuthRequestInterceptor(), is(Optional.empty()));
        assertThat(defaultConfig.client(), is(notNullValue()));
        assertThat(defaultConfig.contract(), is(notNullValue()));
        assertThat(defaultConfig.decoder(), is(notNullValue()));
        assertThat(defaultConfig.encoder(), is(notNullValue()));
        assertThat(defaultConfig.errorDecoder(), is(notNullValue()));
        assertThat(defaultConfig.logger(), is(notNullValue()));
        assertThat(defaultConfig.logLevel(), is(notNullValue()));
        assertThat(defaultConfig.options(), is(notNullValue()));
        assertThat(defaultConfig.requestInterceptors(), is(notNullValue()));
        assertThat(defaultConfig.requestInterceptors(), hasSize(0));
        assertThat(defaultConfig.retryer(), is(notNullValue()));
        assertThat(defaultConfig.setterFactory(), is(notNullValue()));

    }

    @Test
    public void itShouldBePossibleToCustomizeClientConfiguration() {
        ConfigurableClientConfig<AsgRegisterClient> customConfig = ConfigurableClientConfig.<AsgRegisterClient>builder()
                .target(new Target.HardCodedTarget<>(AsgRegisterClient.class, "https://www.example.com"))
                .logLevel(Logger.Level.HEADERS)
                .retryer(Retryer.NEVER_RETRY)
                .requestInterceptor(new RequestInterceptor() {
                    @Override
                    public void apply(RequestTemplate template) {
                        template.header("X-MyCustomHeader", "myCustomValue");
                    }
                })
                .requestInterceptor(TrafficsoftClients.getListRequestInterceptor())
                .setterFactory(new SetterFactory() {
                    @Override
                    public HystrixCommand.Setter create(Target<?> target, Method method) {
                        String groupKey = target.name();
                        String commandKey = Feign.configKey(target.type(), method);

                        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                                .withCoreSize(2);

                        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                                .withFallbackEnabled(false)
                                .withExecutionTimeoutEnabled(true)
                                .withExecutionTimeoutInMilliseconds((int) SECONDS.toMillis(45))
                                .withExecutionIsolationStrategy(THREAD)
                                .withExecutionIsolationThreadInterruptOnTimeout(true);

                        return HystrixCommand.Setter
                                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                                .andThreadPoolPropertiesDefaults(threadPoolProperties)
                                .andCommandPropertiesDefaults(commandProperties);
                    }
                })
                .build();

        assertThat(customConfig, is(notNullValue()));

        assertThat(customConfig.logLevel(), is(Logger.Level.HEADERS));
        assertThat(customConfig.retryer(), is(Retryer.NEVER_RETRY));
        assertThat(customConfig.requestInterceptors(), hasSize(2));
    }
}
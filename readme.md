amv-trafficsoft-rest
========


# build
```
./gradlew clean build
```

# usage

## xfcd
```
String baseUrl = "http//www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
XfcdClient xfcdClient = TrafficsoftClients.xfcd(basicAuth);
```

## asg-register
```
String baseUrl = "http//www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
AsgRegisterClient asgRegisterClient = TrafficsoftClients.asgRegister(basicAuth);
```

# custom configuration
It is possible to apply a custom configuration and configure the clients to your needs. 
If you construct your own config you have to provide the `target` property. e.g.
```
ConfigurableClientConfig<AsgRegisterClient> customConfig = ConfigurableClientConfig.<AsgRegisterClient>builder()
    .target(new Target.HardCodedTarget<>(AsgRegisterClient.class, "http://www.example.com"))
    .logLevel(Logger.Level.HEADERS)
    .retryer(Retryer.NEVER_RETRY)
    .requestInterceptor(new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate template) {
            template.header("X-MyCustomHeader", "MyCustomValue");
        }
    })
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
```
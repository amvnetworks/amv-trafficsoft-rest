[![Build Status](https://travis-ci.org/amvnetworks/amv-trafficsoft-rest.svg?branch=master)](https://travis-ci.org/amvnetworks/amv-trafficsoft-rest)
[![Download](https://api.bintray.com/packages/amvnetworks/amv-trafficsoft-rest/amv-trafficsoft-rest-client/images/download.svg) ](https://bintray.com/amvnetworks/amv-trafficsoft-rest/amv-trafficsoft-rest-client/_latestVersion)

amv-trafficsoft-rest
========


# build
```
./gradlew clean build
```

## release to bintray
```
./gradlew clean build -Prelease -PbintrayUser=${username} -PbintrayApiKey=${apiKey} bintrayUpload
```


# usage

## xfcd
```
String baseUrl = "http//www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
    
XfcdClient xfcdClient = TrafficsoftClients.xfcd(baseUrl, basicAuth);
// ...
```

## asg-register
```
String baseUrl = "http//www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
    
AsgRegisterClient asgRegisterClient = TrafficsoftClients.asgRegister(baseUrl, basicAuth);
// ...
```

# install
## gradle
### repo
```
repositories {
    jcenter()
    // ... or add bintray repo
    maven {
        url  "http://dl.bintray.com/amvnetworks/amv-trafficsoft-rest" 
    }
}
```
### dependency
```
dependencies {
    compile 'org.amv.trafficsoft:amv-trafficsoft-rest-model:${version}'
}
```

## maven 
### repo
```
<profiles>
    <profile>
        <repositories>
            <repository>
                <snapshots>
                    <enabled>false</enabled>
                </snapshots>
                <id>bintray-amvnetworks-amv-trafficsoft-rest</id>
                <name>bintray</name>
                <url>http://dl.bintray.com/amvnetworks/amv-trafficsoft-rest</url>
            </repository>
        </repositories>
        <pluginRepositories>
            <pluginRepository>
                <snapshots>
                    <enabled>false</enabled>
                </snapshots>
                <id>bintray-amvnetworks-amv-trafficsoft-rest</id>
                <name>bintray-plugins</name>
                <url>http://dl.bintray.com/amvnetworks/amv-trafficsoft-rest</url>
            </pluginRepository>
        </pluginRepositories>
        <id>bintray</id>
    </profile>
</profiles>
<activeProfiles>
    <activeProfile>bintray</activeProfile>
</activeProfiles>
```
### dependency
```
<dependency>
  <groupId>org.amv.trafficsoft</groupId>
  <artifactId>amv-trafficsoft-rest-client</artifactId>
  <version>${version}</version>
</dependency>
```

# custom configuration
It is possible to apply a custom configuration and configure the clients to your needs. 
If you construct your own config you have to provide the `target` property
or use `TrafficsoftClients.config(clazz, baseUrl, basicAuth)` method. e.g.
```
String baseUrl = "http//www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
    
ClientConfig<XfcdClient> customConfig = TrafficsoftClients.config(XfcdClient.class, baseUrl, basicAuth)
    .logLevel(Logger.Level.HEADERS)
    .retryer(Retryer.NEVER_RETRY)
    .requestInterceptor(new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate template) {
            template.header("X-MyCustomHeader", "MyCustomValue");
        }
    })
    .requestInterceptor(new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate template) {
            template.header("X-MyCustomHeader", "MyCustomValue");
        }
    })
    .requestInterceptor(new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate template) {
            template.replaceQueryValues(ImmutableMap.<String, String>builder()
                    .put("myQueryParam", "myQueryValue")
            .build());
        }
    })
    // ...
    .build();
    
XfcdClient xfcdClient = TrafficsoftClients.xfcd(customConfig);
// ...
```

### adapt circuit breaker config
This library uses [Hystrix](https://github.com/Netflix/Hystrix/) for latency and fault tolerance.
To adapt the default options you can provide your own `SetterFactory` instance.
For more information see the [Hystrix Configuration Documentation](https://github.com/Netflix/Hystrix/wiki/Configuration).
```
String baseUrl = "http//www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
    
ClientConfig<XfcdClient> customConfig = TrafficsoftClients.config(XfcdClient.class, baseUrl, basicAuth)
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
    
XfcdClient xfcdClient = TrafficsoftClients.xfcd(customConfig);
// ...
```
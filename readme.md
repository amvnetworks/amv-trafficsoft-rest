[![Build Status](https://travis-ci.org/amvnetworks/amv-trafficsoft-rest.svg?branch=master)](https://travis-ci.org/amvnetworks/amv-trafficsoft-rest)
[![Download](https://api.bintray.com/packages/amvnetworks/amv-trafficsoft-rest/amv-trafficsoft-rest-client/images/download.svg) ](https://bintray.com/amvnetworks/amv-trafficsoft-rest)

amv-trafficsoft-rest
========
amv-trafficsoft-rest is a Java client library for accessing the AMV Trafficsoft API.

amv-trafficsoft-rest requires Java version 1.8 or greater.

# build
```
./gradlew clean build
```

## release to bintray
```
./gradlew clean build -Prelease -PbintrayUser=${username} -PbintrayApiKey=${apiKey} bintrayUpload
```


# usage

## constructing clients

Construct a new client for the various services to access different parts of the AMV Trafficsoft API.

### xfcd
```
String baseUrl = "http://www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
    
XfcdClient xfcdClient = TrafficsoftClients.xfcd(baseUrl, basicAuth);
// ...
```

### asg-register
```
String baseUrl = "http://www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
    
AsgRegisterClient asgRegisterClient = TrafficsoftClients.asgRegister(baseUrl, basicAuth);
// ...
```

## execution
Every method returns a [HystrixCommand](https://netflix.github.io/Hystrix/javadoc/index.html?com/netflix/hystrix/HystrixCommand.html) 
which is essentially a blocking command but provides non-blocking execution if used with `toObservable()`. 
See [Hystrix wiki](https://github.com/Netflix/Hystrix/wiki/How-it-Works#flow2) for more information.

### non-blocking
Used for asynchronous execution of a request with a callback by subscribing to the Observable.
```
long contractId = 42;

Action1<OemsResponseRestDto> onNext = oemsResponseRestDto -> {
    log.info("Received: {}", oemsResponseRestDto);
};
Action1<Throwable> onError = error -> {
    log.error("{}", error);
};
Action0 onComplete = () -> {
    log.info("Completed.");
};

myAsgRegisterClient.getOems(contractId)
    .toObservable()
    .subscribe(onNext, onError, onCompleted);
// ...
```

### blocking
Used for synchronous execution of requests.
```
long contractId = 42;

OemsResponseRestDto oemsResponseDto = myAsgRegisterClient
    .getOems(contractId)
    .execute();
// ...
```

## examples
Take a look at the [test/](amv-trafficsoft-rest-client/src/test/java/org/amv/trafficsoft/rest/client/) directory 
as a good way of getting started and a quick overview of the key client features and concepts.

### demo application
See [amv-trafficsoft-restclient-demo](https://github.com/amvnetworks/amv-trafficsoft-restclient-demo) repository for a
simple demo application.

###

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
    compile 'org.amv.trafficsoft:amv-trafficsoft-rest-client:${version}'
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
String baseUrl = ...
BasicAuth basicAuth = ...
    
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
The clients are created with reasonable default values but the options can be adapted to your special 
requirements by providing your own `SetterFactory` instance.
For more information see the [Hystrix Configuration Documentation](https://github.com/Netflix/Hystrix/wiki/Configuration).
```
String baseUrl = ...
BasicAuth basicAuth = ...
    
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

### adapt http client
This library uses [OkHttpClient](https://github.com/OpenFeign/feign/tree/master/okhttp) 
as default client to direct http requests to [OkHttp](http://square.github.io/okhttp/), 
which enables SPDY and better network control. You can easily switch to another client like 
[ApacheHttpClient](https://github.com/OpenFeign/feign/tree/master/httpclient) in order to use 
[Apache HttpComponents](https://hc.apache.org/httpcomponents-client-ga/) or provide your very 
own implementation:
```
String baseUrl = ...
BasicAuth basicAuth = ...
    
ClientConfig<XfcdClient> customConfig = TrafficsoftClients.config(XfcdClient.class, baseUrl, basicAuth)
    .client(new ApacheHttpClient())
    // ...
    .build();
    
XfcdClient xfcdClient = TrafficsoftClients.xfcd(customConfig);
// ...
```
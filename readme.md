amv-trafficsoft-rest
========


# build
```
./gradlew clean build
```

# usage
```
String baseUrl = "http//www.example.com";
BasicAuth basicAuth = BasicAuthImpl.builder()
    .username("john_doe")
    .password("mysupersecretpassword")
    .build();
XfcdClient client = TrafficsoftClients.xfcd(basicAuth);
```
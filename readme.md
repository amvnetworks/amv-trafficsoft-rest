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

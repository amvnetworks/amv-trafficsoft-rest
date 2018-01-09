package org.amv.trafficsoft.rest.client.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "amv.trafficsoft.api.rest")
public class TrafficsoftApiRestProperties {
    private String baseUrl;
    private String username;
    private String password;
    private long contractId;
}

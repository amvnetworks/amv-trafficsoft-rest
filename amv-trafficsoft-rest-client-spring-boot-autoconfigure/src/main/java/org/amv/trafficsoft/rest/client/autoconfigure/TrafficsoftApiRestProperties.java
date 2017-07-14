package org.amv.trafficsoft.rest.client.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "amv.trafficsoft.api.rest")
public class TrafficsoftApiRestProperties {
    private String baseUrl;
    private String username;
    private String password;
    private long contractId;
    private List<Long> vehicleIds;
}

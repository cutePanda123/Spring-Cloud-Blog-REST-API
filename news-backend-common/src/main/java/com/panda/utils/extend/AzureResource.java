package com.panda.utils.extend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:azure.properties")
@ConfigurationProperties(prefix = "azure.communication-service")
public class AzureResource {
    private String connectionString;
}

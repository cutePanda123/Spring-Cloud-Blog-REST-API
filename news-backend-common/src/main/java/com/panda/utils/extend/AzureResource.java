package com.panda.utils.extend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:azure.properties")
@ConfigurationProperties(prefix = "azure")
public class AzureResource {
}

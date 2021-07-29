package com.jay.instagram.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "instagram.server")
@Configuration
public class ServerConfig {
    private String serverDirPath;
    private String imgSrcPath;
}

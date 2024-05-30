package kz.aptekaplus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties(
        String bucketName,
        String endpoint,
        String accessKey,
        String secretKey
) {}

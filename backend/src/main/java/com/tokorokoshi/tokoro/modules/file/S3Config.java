package com.tokorokoshi.tokoro.modules.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Bean
    public FileStorageService fileStorageService() {
        return new S3FileStorageService(accessKey, secretKey, region, bucketName);
    }

}
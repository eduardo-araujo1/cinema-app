package com.eduardo.cinema_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
public class AwsS3Config {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretAccessKey;

    @Bean
    public S3Client createS3Instance() {
        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .region(Region.of(awsRegion));

        s3ClientBuilder.credentialsProvider(StaticCredentialsProvider.create
                (AwsBasicCredentials.create
                        (accessKeyId, secretAccessKey)));

        return s3ClientBuilder.build();
    }

}


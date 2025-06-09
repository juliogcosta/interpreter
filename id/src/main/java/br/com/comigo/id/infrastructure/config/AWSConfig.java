package br.com.comigo.id.infrastructure.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    /*@Value("${aws.region}")
    private String awsRegion;
    @Value("${aws.accessKey:}")
    private String accessKeyId;
    @Value("${aws.secretKey:}")
    private String secretAccessKey;

    @Bean
    public S3Client createS3Instance() {
        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .region(Region.of(awsRegion));

        if (accessKeyId != null && !accessKeyId.isEmpty() &&
                secretAccessKey != null && !secretAccessKey.isEmpty()) {
            s3ClientBuilder.credentialsProvider(
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)));
        }

        return s3ClientBuilder.build();
    }*/
}

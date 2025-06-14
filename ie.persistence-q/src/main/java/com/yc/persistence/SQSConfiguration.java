package com.yc.persistence;

//@Configuration
public class SQSConfiguration
{
    /*
    @Value("${yc.aws.queue.persistence-buffer.region.static}")
    private String awsRegion;

    @Value("${yc.aws.queue.persistence-buffer.credentials.access-key}")
    private String awsAccessKey;

    @Value("${yc.aws.queue.persistence-buffer.credentials.secret-key}")
    private String awsSecretKey;

    @Bean
    QueueMessagingTemplate queueMessagingTemplate()
    {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    RegionProvider regionProvider() 
    {
        return new StaticRegionProvider(this.awsRegion);
    }

    @Primary
    @Bean
    AmazonSQSAsync amazonSQSAsync()
    {
        return AmazonSQSAsyncClientBuilder.standard()
                .withRegion(this.awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.awsAccessKey, this.awsSecretKey)))
                .build();
    }
     */
}

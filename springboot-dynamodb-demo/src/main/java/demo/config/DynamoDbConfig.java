package demo.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import demo.config.properties.AwsProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
@EnableDynamoDBRepositories(basePackages = "demo.repository")
@RequiredArgsConstructor
public class DynamoDbConfig {

    private final AwsProperties awsProperties;

    @Primary
    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB(), DynamoDBMapperConfig.DEFAULT);
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsProperties.getCredential().getAccessKey(),
                                        awsProperties.getCredential().getSecretKey()));
        final EndpointConfiguration endpointConfiguration = new EndpointConfiguration(
                awsProperties.getDynamodb().getEndpoint(),
                awsProperties.getDynamodb().getRegion());

        return AmazonDynamoDBClientBuilder.standard()
                                          .withCredentials(credentialsProvider)
                                          .withEndpointConfiguration(endpointConfiguration)
                                          .build();
    }
}

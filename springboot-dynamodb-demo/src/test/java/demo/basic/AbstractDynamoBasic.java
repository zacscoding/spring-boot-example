package demo.basic;

import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import demo.util.LogLevelUtil;

@Testcontainers
public class AbstractDynamoBasic {

    static final String DOCKER_IMAGE = "amazon/dynamodb-local:latest";
    static final int PORT = 8000;

    static {
        LogLevelUtil.setWarn("org.testcontainers");
        LogLevelUtil.setWarn("com.github.dockerjava");
        LogLevelUtil.setInfo();
    }

    @Container
    protected final GenericContainer dynamodb = new GenericContainer(DOCKER_IMAGE)
            .withExposedPorts(PORT)
            .withCommand("-jar", "DynamoDBLocal.jar", "-sharedDb", "-inMemory");

    protected AmazonDynamoDB client;
    protected DynamoDBMapper mapper;

    @BeforeEach
    public void setUp() {
        final AWSCredentials awsCredentials = new BasicAWSCredentials("dummy", "dummy");
        final AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        final String endpoint = String.format("http://%s:%s", dynamodb.getContainerIpAddress(),
                                              dynamodb.getMappedPort(PORT));
        final EndpointConfiguration endpointConfiguration = new EndpointConfiguration(endpoint, "ap-northeast-2");

        client = AmazonDynamoDBClientBuilder.standard()
                                            .withCredentials(awsCredentialsProvider)
                                            .withEndpointConfiguration(endpointConfiguration)
                                            .build();

        mapper = new DynamoDBMapper(client, DynamoDBMapperConfig.DEFAULT);
    }
}

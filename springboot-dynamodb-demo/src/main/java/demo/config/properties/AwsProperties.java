package demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "aws", ignoreInvalidFields = true)
@Data
public class AwsProperties {

    private DynamoDbProperties dynamodb;
    private Credential credential;

    @Data
    public static class DynamoDbProperties {
        private String endpoint;
        private String region;
    }

    @Data
    public static class Credential {
        private String accessKey;
        private String secretKey;
    }
}

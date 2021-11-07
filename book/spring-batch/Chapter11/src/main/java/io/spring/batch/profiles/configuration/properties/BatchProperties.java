package io.spring.batch.profiles.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(prefix = "batch", ignoreInvalidFields = true)
@Getter
@Setter
@ToString
public class BatchProperties {

    private int customerUpdateChunkSize;
    private int importTransactionChunkSize;
    private int applyTransactionChunkSize;
    private Profile profiles;

    @Getter
    @Setter
    @ToString
    public static class Profile {
        private boolean cpuEnabled;
        private boolean memoryEnabled;
    }
}

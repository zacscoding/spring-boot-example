package demo.config.properties;

import demo.config.condition.DatabaseEnabledCondition.LevelDbEnabledCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ConfigurationProperties(prefix = "leveldb")
@Conditional(value = {LevelDbEnabledCondition.class})
public class LevelDbProperties {

    private String name;
    private String dir;

    @Builder.Default
    private int blockSize = 4 * 1024;
    @Builder.Default
    private int writeBufferSize = 4 << 20;
    @Builder.Default
    private long cacheSize = 0L;
    @Builder.Default
    private int maxOpenFiles = 20;
}

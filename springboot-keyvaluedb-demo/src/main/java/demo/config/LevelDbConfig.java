package demo.config;

import demo.config.condition.DatabaseEnabledCondition.LevelDbEnabledCondition;
import demo.config.properties.LevelDbProperties;
import demo.database.DbSource;
import demo.database.leveldb.LevelDbDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Slf4j
@Configuration
@Conditional(value = {LevelDbEnabledCondition.class})
public class LevelDbConfig {

    private LevelDbProperties levelDbProperties;

    @Autowired
    public LevelDbConfig(LevelDbProperties levelDbProperties) {
        this.levelDbProperties = levelDbProperties;
    }

    @Bean
    public DbSource<byte[]> dbSource() {
        LevelDbDataSource levelDbDataSource = new LevelDbDataSource(levelDbProperties);
        levelDbDataSource.init();
        return levelDbDataSource;
    }
}

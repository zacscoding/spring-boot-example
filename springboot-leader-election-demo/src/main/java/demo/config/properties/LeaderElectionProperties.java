package demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "cluster", ignoreInvalidFields = true)
@Getter
@Setter
public class LeaderElectionProperties {

    private String id;
    private LeaderShip leaderShip;

    @Getter
    @Setter
    public static class LeaderShip {

        private boolean enable;
        private String type;
        private String lockName;
        private Redis redis;
        private Zookeeper zookeeper;
    }

    @Getter
    @Setter
    public static class Redis {

        private String address;
        private int idleConnectionTimeout;
    }

    @Getter
    @Setter
    public static class Zookeeper {

        private String connectString;
        private int maxRetries;
        private int sleepMsBetweenRetries;
    }
}

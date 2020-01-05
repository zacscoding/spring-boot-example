package demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

/**
 *
 */
public class SayHelloConfiguration {

    private IClientConfig ribbonClientConfig;

    @Autowired
    public SayHelloConfiguration(IClientConfig ribbonClientConfig) {

        System.out.println("## ribbonClientConfig : " + ribbonClientConfig);
        this.ribbonClientConfig = ribbonClientConfig;
        System.out.println("## ribbonClientConfig : " + ribbonClientConfig);
    }

    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl(false, "alive");
        //return new PingUrl();
    }

    @Bean
    public IRule ribbonRule(IClientConfig config) {
        return new AvailabilityFilteringRule();
    }
}

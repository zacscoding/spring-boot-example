package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.loadbalancer.AbstractLoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.ServerList;

import demo.client.GroupRoundRobinRule;
import demo.client.ServiceServer;
import demo.client.lowercase.LowerServiceList;
import demo.client.persist.ServiceRepository;
import demo.client.uppercase.UpperServiceList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("client")
@Configuration
@ComponentScan("demo.client")
@Slf4j
@RequiredArgsConstructor
public class ClientConfiguration {

    // ============= tag : upper

    @Bean
    public IPing upperServiceIPing() {
        return new PingUrl(false, "/alive");
    }

    @Bean
    public GroupRoundRobinRule upperGroupRoundRobinRule() {
        return new GroupRoundRobinRule();
    }

    @Bean
    public AbstractLoadBalancer upperLoadBalancer(ServiceRepository repository) {
        LoadBalancerBuilder<ServiceServer> builder = LoadBalancerBuilder.newBuilder();
        return builder
                .withPing(upperServiceIPing())
                .withRule(upperGroupRoundRobinRule())
                .withDynamicServerList(upperServiceList(repository))
                .buildDynamicServerListLoadBalancer();
    }

    @Bean
    public ServerList<ServiceServer> upperServiceList(ServiceRepository repository) {
        return new UpperServiceList(repository);
    }

    // ============= tag : lower

    @Bean
    public IPing lowerServiceIPing() {
        return new PingUrl(false, "/alive");
    }

    @Bean
    public ServerList<ServiceServer> lowerServiceList(ServiceRepository repository) {
        return new LowerServiceList(repository);
    }

    @Bean
    public GroupRoundRobinRule lowerGroupRoundRobinRule() {
        return new GroupRoundRobinRule();
    }

    @Bean
    public AbstractLoadBalancer lowerLoadBalancer(ServiceRepository repository) {
        LoadBalancerBuilder<ServiceServer> builder = LoadBalancerBuilder.newBuilder();
        return builder
                .withClientConfig(new DefaultClientConfigImpl())
                .withPing(lowerServiceIPing())
                .withRule(lowerGroupRoundRobinRule())
                .withDynamicServerList(lowerServiceList(repository))
                .buildDynamicServerListLoadBalancer();
    }
}

package demo.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.loadbalancer.AbstractLoadBalancer;

import demo.client.lowercase.LowerCaseService;
import demo.client.persist.ServiceEntity;
import demo.client.persist.ServiceRepository;
import demo.client.uppercase.UpperCaseService;
import demo.common.MessageRequest;
import feign.Feign;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;

/**
 */
@RestController
@Slf4j
public class ClientController {

    // required
    private final AbstractLoadBalancer lowerServiceLb;
    private final AbstractLoadBalancer upperServiceLb;
    private final ServiceRepository repository;

    // build
    private LowerCaseService lowerCaseService;
    private UpperCaseService upperCaseService;

    public ClientController(@Qualifier("upperLoadBalancer") AbstractLoadBalancer upperServiceLb,
                            @Qualifier("lowerLoadBalancer") AbstractLoadBalancer lowerServiceLb,
                            ServiceRepository repository) {

        this.upperServiceLb = upperServiceLb;
        this.lowerServiceLb = lowerServiceLb;
        this.repository = repository;
    }

    @PostConstruct
    private void setUp() {
        lowerCaseService =
                Feign.builder()
                     .logger(new Slf4jLogger())
                     .retryer(new Retryer.Default(1, 1, 1))
                     .encoder(new GsonEncoder())
                     .decoder(new GsonDecoder())
                     .target(RibbonLoadBalancingTarget.create(LowerCaseService.class,
                                                              "http://lowerservice",
                                                              lowerServiceLb));

        upperCaseService =
                Feign.builder()
                     .logger(new Slf4jLogger())
                     .retryer(new Retryer.Default(1, 1, 1))
                     .encoder(new GsonEncoder())
                     .decoder(new GsonDecoder())
                     .target(RibbonLoadBalancingTarget.create(UpperCaseService.class,
                                                              "http://upperservice",
                                                              upperServiceLb));
    }

    @GetMapping("/{groupName}/lower/{message}")
    public String toLower(@PathVariable("groupName") String groupName,
                          @PathVariable("message") String message) {

        logger.info(">> /{}/lower/{}", groupName, message);
        return lowerCaseService.call(groupName, new MessageRequest(message));
    }

    @GetMapping("/{groupName}/upper/{message}")
    public String toUpper(@PathVariable("groupName") String groupName,
                          @PathVariable("message") String message) {

        logger.info(">> /{}/upper/{}", groupName, message);
        return upperCaseService.call(groupName, new MessageRequest(message));
    }

    @PostMapping("/service")
    @Transactional
    public String addService(@RequestBody ServiceEntity serviceEntity) {
        logger.info("Try to add a service : {}", serviceEntity.toString());
        serviceEntity.setId(null);
        return repository.save(serviceEntity).toString();
    }
}

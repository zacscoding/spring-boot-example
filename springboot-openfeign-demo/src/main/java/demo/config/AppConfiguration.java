package demo.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import demo.client.persist.ServiceEntity;
import demo.client.persist.ServiceRepository;
import demo.client.persist.ServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("client")
@Configuration
@Slf4j
@RequiredArgsConstructor
public class AppConfiguration {

    private final InitService initService;

    @PostConstruct
    private void setUp() {
        logger.info(">>>>>>> Setup data store");
        initService.initDb();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        final ServiceRepository serviceRepository;
        final String schema = "http";
        final String host = "127.0.0.1";

        public void initDb() {
            // group1
            saveServiceEntity(ServiceType.UPPER_CASE_SERVICE, "group1", 3011);
            saveServiceEntity(ServiceType.UPPER_CASE_SERVICE, "group1", 3012);

            // group2
            saveServiceEntity(ServiceType.UPPER_CASE_SERVICE, "group2", 3021);
            saveServiceEntity(ServiceType.UPPER_CASE_SERVICE, "group2", 3022);
        }

        private void saveServiceEntity(ServiceType serviceType, String groupName, int port) {
            final ServiceEntity entity = ServiceEntity.builder()
                                                      .serviceType(serviceType)
                                                      .groupName(groupName)
                                                      .schema(schema)
                                                      .host(host)
                                                      .port(port)
                                                      .build();
            System.out.println("> Save service " + serviceRepository.save(entity));
        }
    }
}
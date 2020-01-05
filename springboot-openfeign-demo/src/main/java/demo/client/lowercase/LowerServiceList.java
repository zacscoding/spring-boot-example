package demo.client.lowercase;

import java.util.List;
import java.util.stream.Collectors;

import com.netflix.loadbalancer.ServerList;

import demo.client.ServiceServer;
import demo.client.persist.ServiceRepository;
import demo.client.persist.ServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LowerServiceList implements ServerList<ServiceServer> {

    private final ServiceRepository repository;

    @Override
    public List<ServiceServer> getUpdatedListOfServers() {
        return repository.findByServiceType(ServiceType.LOWER_CASE_SERVICE)
                         .stream()
                         .map(e -> new ServiceServer(e.getSchema(), e.getHost(), e.getPort(),
                                                     e.getGroupName()))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ServiceServer> getInitialListOfServers() {
        return repository.findByServiceType(ServiceType.LOWER_CASE_SERVICE)
                         .stream()
                         .map(e -> new ServiceServer(e.getSchema(), e.getHost(), e.getPort(),
                                                     e.getGroupName()))
                         .collect(Collectors.toList());
    }
}

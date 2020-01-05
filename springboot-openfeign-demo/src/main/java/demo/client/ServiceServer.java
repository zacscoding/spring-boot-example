package demo.client;

import com.netflix.loadbalancer.Server;

import lombok.Getter;

@Getter
public class ServiceServer extends Server {

    private String groupName;

    public ServiceServer(String host, int port, String groupName) {
        super(host, port);
        this.groupName = groupName;
    }

    public ServiceServer(String scheme, String host, int port, String groupName) {
        super(scheme, host, port);
        this.groupName = groupName;
    }

    public ServiceServer(String id, String groupName) {
        super(id);
        this.groupName = groupName;
    }
}

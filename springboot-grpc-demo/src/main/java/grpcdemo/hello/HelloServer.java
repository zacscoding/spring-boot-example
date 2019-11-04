package grpcdemo.hello;

import io.grpc.Server;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class HelloServer extends Thread {

    private Server server;
    private HelloServiceProperties properties;

    public HelloServer(HelloServiceProperties properties) {
        this.properties = properties;
    }
}

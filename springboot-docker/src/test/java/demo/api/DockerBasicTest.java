package demo.api;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import demo.util.GsonUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * https://www.baeldung.com/docker-java-api
 *
 * @author zacconding
 * @Date 2018-10-13
 * @GitHub : https://github.com/zacscoding
 */
public class DockerBasicTest {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    private DockerClient dockerClient;

    @Before
    public void setUp() {
        logger.setLevel(Level.INFO);
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                                                                    .withRegistryPassword("apppw")
                                                                    .withRegistryUsername("app")
                                                                    .withDockerHost("tcp://192.168.79.128:2376")
                                                                    .build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    @After
    public void tearDown() throws IOException {
        dockerClient.close();
    }

    @Test
    public void containerWith() throws Exception {
        /*
        $ docker ps -a -s -f status=exited
        or
        $ docker container ls -a -s -f status=exited
         */
        List<Container> containers = dockerClient.listContainersCmd()
                                                 .withShowSize(true)
                                                 .withShowAll(true)
                                                 .withStatusFilter(Arrays.asList("exited"))
                                                 .exec();
        for (Container container : containers) {
            System.out.println("------------------------------------------------------------------------------");
            GsonUtil.printGsonPretty(container);
            System.out.println("------------------------------------------------------------------------------");
        }
    }

    @Test
    public void createContainer() {
        /*
        $ docker create --name mongo \
        --hostname=mongoapp \
        -e MONGO_LATEST_VERSION=3.6 \
        -p 9999:27017 \
        -v /home/app/mongo/data/db:/data/db \
        mongo:3.6 --bind_ip_all
         */

        CreateContainerResponse container = dockerClient.createContainerCmd("mongo:3.6")
            .withCmd("--bind_ip_all")
            .withName("mongo")
            .withHostName("mongoapp")
            .withEnv("MONGO_LATEST_VERSION=3.6")
            .withPortBindings(PortBinding.parse("9999:27017"))
            .withBinds(Bind.parse("/home/app/mongo/data/db:/data/db"))
            .exec();

        List<Container> containers = dockerClient.listContainersCmd()
                                                 .withNameFilter(Arrays.asList("mongoapp"))
                                                 .exec();

        displayContainer(containers);
    }

    @Test
    public void startStopKill() {
        List<Container> containers = dockerClient.listContainersCmd()
                                                 .withShowAll(true)
                                                 .exec();

        String containerId = containers.get(0).getId();

        // start
        dockerClient.startContainerCmd(containerId).exec();

        // stop
        // dockerClient.stopContainerCmd(containerId).exec();

        // kill
        // dockerClient.killContainerCmd(containerId).exec();
    }

    @Test
    public void inspect() {
        List<Container> containers = dockerClient.listContainersCmd()
                                                 .withShowAll(true)
                                                 .exec();

        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containers.get(0).getId()).exec();

        GsonUtil.printGsonPretty(containerResponse);
    }

    private void displayContainer(List<Container> containers) {
        for (Container container : containers) {
            System.out.println("------------------------------------------------------------------------------");
            GsonUtil.printGsonPretty(container);
            System.out.println("------------------------------------------------------------------------------");
        }
    }
}
package demo.person.first;

import static java.lang.String.format;

import java.io.File;
import java.util.Arrays;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * Person integration tests with testcontainers
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")*/
public class PersonControllerTest {

    static final String POSTGRES_SERVICE_NAME = "postgres_test_db";
    static final Integer POSTGRES_PORT = 5432;

    /*@ClassRule
    public static DockerComposeContainer environment =
        new DockerComposeContainer(new File("src/test/resources/compose/postgresql-compose.yaml"))
            .withExposedService(POSTGRES_SERVICE_NAME, POSTGRES_PORT, Wait.forListeningPort());*/

    /*@Test
    public void temp() {
        String serviceHost = environment.getServiceHost(POSTGRES_SERVICE_NAME, POSTGRES_PORT);
        System.out.println(
            serviceHost
        );
    }*/

    private void temp(Integer internalPort) {
        String[][] commands = {
            {"/bin/sh", "-c", format("cat /proc/net/tcp{,6} | awk \'{print $2}\' | grep -i :%x", internalPort)},
            {"/bin/sh", "-c", format("nc -vz -w 1 localhost %d", internalPort)},
            {"/bin/bash", "-c", format("</dev/tcp/localhost/%d", internalPort)}
        };

        for (String[] command : commands) {
            StringBuilder sb = new StringBuilder();
            for (String cmd : command) {
                sb.append(cmd).append(" ");
            }
            System.out.println(sb);
        }
    }
}

package demo;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.echo.EchoRequest;
import demo.echo.EchoResponse;

@RestController
@SpringBootApplication
public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    public DemoApplication(Environment environment) {this.environment = environment;}

    @PostMapping("/echo")
    public EchoResponse echo(EchoRequest request) {
        logger.info("sayHello is called in server\n{}", request);
        return new EchoResponse(UUID.randomUUID().toString(),
                                request.getHeader(), request.getMessage());
    }
}

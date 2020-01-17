package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.command.RootCommand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.tools.jar.CommandLine;

@SpringBootApplication
@RestController
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    RootCommand rootCommand;

    @PostMapping("/command")
    public String handleCommand(@RequestBody CommandRequest commandRequest) {
        String[] args = commandRequest.getCommandLine().split("\\s+");

        logger.info("receive command : {}", commandRequest.getCommandLine());

        return rootCommand.executeCommand(args);
    }

    @Data
    static class CommandRequest {

        String commandLine;
    }
}

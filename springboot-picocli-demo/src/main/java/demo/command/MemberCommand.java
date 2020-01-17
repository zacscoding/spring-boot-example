package demo.command;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Member command handler
 */
@Component
@Command(name = "member", mixinStandardHelpOptions = true, subcommands = MemberCommand.AddCommand.class)
@Slf4j
@Getter
public class MemberCommand implements Callable<String>, ICommand {

    @Override
    public String call() throws Exception {
        logger.info("MemberCommand:call()");
        return "MemberCommand:call()";
    }

    @Command(name = "add", mixinStandardHelpOptions = true, exitCodeOnExecutionException = 34)
    static class AddCommand implements Callable<String> {

        public AddCommand() {
            logger.info("MemberCommand.AddCommand is created");
        }

        @Option(names = { "-n", "--name" }, description = "name of a member")
        private String name;

        @Option(names = { "-a", "--age" }, description = "age of a member")
        private int age;

        @Option(names = { "-hb", "--hobbies" }, description = "hobbies of a member")
        private String[] hobbies;

        @Override
        public String call() throws Exception {
            logger.info("MemberAddCommand:call() ==> name : {}, age : {}, hobbies : {}",
                        name, age, String.join(",", hobbies));

            return "MemberAddCommand:call()";
        }
    }
}
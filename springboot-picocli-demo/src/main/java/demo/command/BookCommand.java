package demo.command;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "book", mixinStandardHelpOptions = true, subcommands = BookCommand.AddCommand.class)
@Slf4j
@Getter
public class BookCommand implements Callable<String>, ICommand {

    @Override
    public String call() throws Exception {
        throw new UnsupportedOperationException("Not supported book command");
    }

    @Component
    @Command(name = "add", mixinStandardHelpOptions = true, exitCodeOnExecutionException = 34)
    static class AddCommand implements Callable<String> {

        public AddCommand() {
            logger.info("BookCommand.AddCommand is created");
        }

        @Option(names = { "-t", "--title" }, description = "title of a book")
        private String title;

        @Option(names = { "-d", "--description" }, description = "description of a book")
        private String description;

        @Override
        public String call() throws Exception {
            logger.info("Book:AddCommand:call() ==> title : {}, description : {}",
                        title, description);
            return "Book:AddCommand:call()";
        }
    }
}

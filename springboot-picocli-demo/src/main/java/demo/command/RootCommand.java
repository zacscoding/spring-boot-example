package demo.command;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IFactory;

/**
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RootCommand {

    private final Map<String, ICommand> commands = new HashMap<>();

    private final MemberCommand memberCommand;
    private final BookCommand bookCommand;
    private final IFactory factory;

    @PostConstruct
    private void setUp() {
        commands.put(extractCommandName(MemberCommand.class), memberCommand);
        commands.put(extractCommandName(BookCommand.class), bookCommand);
    }

    public String executeCommand(String[] args) {
        if (args == null || args.length == 0) {
            logger.warn("empty args");
            return null;
        }

        ICommand command = commands.get(args[0]);

        if (command == null) {
            logger.warn("Not found command {}", args);
            return null;
        }

        logger.info("find a command given {} >> {}", args[0], command.getClass().getName());

        final String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

        final CommandLine commandLine = new CommandLine(command, factory);
        commandLine.execute(commandArgs);

        return extractCommandResult(commandLine);
    }

    private String extractCommandResult(CommandLine commandLine) {
        if (commandLine.getExecutionResult() != null) {
            return commandLine.getExecutionResult();
        }

        for (CommandLine subCommandLine : commandLine.getSubcommands().values()) {
            String result = extractCommandResult(subCommandLine);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private String extractCommandName(Class<?> clazz) {
        final Command command = clazz.getAnnotation(Command.class);

        if (command == null) {
            throw new RuntimeException("Not exist @Command annotation in " + clazz.getName());
        }

        System.out.println(">> Check " + clazz.getName() + " :: " + command.name());

        return command.name();
    }
}

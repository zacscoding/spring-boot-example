package demo.command;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberCommandTest {

    @Autowired
    RootCommand rootCommand;

    @Test
    public void addMember() {
        final String[] memberAddArgs = {
                "member",
                "add",
                "-n", "memberA",
                "--age", "15",
                "-hb", "coding",
                "-hb", "movie"
        };

        String result = rootCommand.executeCommand(memberAddArgs);
        System.out.println("> Member add result : " + result);

        final String[] bookAddArgs = {
                "book",
                "add",
                "-t", "Zaccoding's Learning spring boot :)",
                "--description", "this is very cheap!"
        };

        result = rootCommand.executeCommand(bookAddArgs);
        System.out.println("> Book add result : " + result);
    }
}
package demo.example;

import org.springframework.shell.standard.ShellMethod;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class AbstractCommand {

    @ShellMethod(value = "abstractCommand..", key = "abstractCommand")
    public int abstractCommand(int a, int b) {
        return a + b;
    }
}

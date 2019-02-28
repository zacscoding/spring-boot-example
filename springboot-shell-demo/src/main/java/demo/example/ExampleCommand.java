package demo.example;

import demo.util.ConsoleUtil;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * https://docs.spring.io/spring-shell/docs/current/reference/htmlsingle/
 */
@Slf4j
@Profile("example")
@ShellComponent
public class ExampleCommand {

    // shell:>add 1 2
    @ShellMethod("Add two numbers")
    public int add(int a, int b) {
        return a + b;
    }

    // shell:>sum 1 2
    @ShellMethod(value = "Add numbers", key = "sum")
    public int add2(int a, int b) {
        return a + b;
    }

    //    shell:>echo 1 2 3
    //    a : 1, b : 2, c : 3
    //    shell:>echo --a 1 --b 2 --c 3
    //    a : 1, b : 2, c : 3
    //    shell:>echo --a 1 2 3
    //    a : 1, b : 2, c : 3
    //    shell:>echo 1 --c 3 2
    //    a : 1, b : 2, c : 3
    @ShellMethod("Display stuff")
    public String echo(int a, int b, int c) {
        return String.format("a : %d, b : %d, c : %d", a, b, c);
    }

    //    shell:>echo2 1 2 3
    //    a : 1, b : 2, c : 3
    //    shell:>echo2 1 2 --third 3
    //    a : 1, b : 2, c : 3
    //    shell:>echo 1 2 --c 3
    //    a : 1, b : 2, c : 3
    //    shell:>echo2 1 --second 2 3
    //    a : 1, b : 2, c : 3
    //    shell:>echo2 1 --third 3 --s 2
    //    a : 1, b : 2, c : 3
    @ShellMethod(value = "Display stuff", key = "echo2", prefix = "-")
    public String echo2(int a, @ShellOption({"--second", "--s"}) int b, @ShellOption("--third") int c) {
        return String.format("a : %d, b : %d, c : %d", a, b, c);
    }

    @ShellMethod(value = "Test")
    public String temp() throws Exception {
        Thread task = new Thread(() -> {
            try {
                int i = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Working... " + i++);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
            }
        });
        task.start();
        ConsoleUtil.waitForInputString(readLine -> "q".equals(readLine));
        task.interrupt();
        return "";
    }
}

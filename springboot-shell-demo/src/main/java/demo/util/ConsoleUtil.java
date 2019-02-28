package demo.util;

import java.util.Scanner;
import java.util.function.Predicate;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class ConsoleUtil {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Read line until Predicate`s test is true
     */
    public static void waitForInputString(Predicate<String> terminatePredicate) {
        while (true) {
            String readLine = scanner.nextLine();
            if (terminatePredicate.test(readLine)) {
                break;
            }
        }
    }
}

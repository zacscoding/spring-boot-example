package demo.common;

/**
 *
 */
public class TestHelper {

    public static void out(String format, Object... args) {
        System.out.flush();
        System.err.flush();

        System.out.println(String.format(format, args));

        System.err.flush();
        System.out.flush();
    }
}

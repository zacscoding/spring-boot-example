package demo.helper;

/**
 *
 */
public class TestHelper {

    public static void doTask(String title, TestTask task) {
        printfln("===============================================");
        printfln("> %s", title);
        printfln("===============================================");
        try {
            task.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static void printfln(String format, Object... args) {
        System.out.flush();
        System.err.flush();

        System.out.printf(format, args);
        System.out.println();

        System.out.flush();
        System.err.flush();
    }

    @FunctionalInterface
    public interface TestTask {

        void run() throws Exception;
    }
}

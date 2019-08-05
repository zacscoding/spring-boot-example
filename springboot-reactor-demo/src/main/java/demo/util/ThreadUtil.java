package demo.util;

import java.io.PrintStream;

/**
 *
 */
public class ThreadUtil {

    private static String NEW_LINE = System.getProperty("line.separator");
    private static PrintStream PS = System.out;

    public static String getStackTraceString(int cursor) {
        StackTraceElement[] elts = Thread.currentThread().getStackTrace();
        if (elts == null || elts.length == 1) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int start, size;
        if (cursor >= 0) {
            start = cursor + 2;
            size = elts.length;
        } else {
            start = 2;
            size = start - cursor + 1;
        }

        return getStackTraceString(elts, start, size);
    }

    public static String getStackTraceString(StackTraceElement[] se, int start, int size) {
        if (se == null) {
            return "";
        }

        if (size < 0) {
            size = 0;
        }
        size = Math.min(size, se.length);
        if (start >= size) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < size; i++) {
            sb.append("\t").append(se[i].toString());
            if (i != size - 1) {
                sb.append(NEW_LINE);
            }
        }

        return sb.toString();
    }

    public static void printStackTrace() {
        PS.println(getStackTraceString(2));
    }

    private ThreadUtil() {
    }
}

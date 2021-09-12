package io.spring.batch.util;

public final class ThreadUtil {

    public static String getStackTrace() {
        final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 2; i < elements.length; i++) {
            stringBuilder.append(elements[i].toString());
            if (i != elements.length - 1) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    private ThreadUtil() {}
}

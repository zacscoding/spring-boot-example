package org.zaccoding.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintStream;

/**
 * @author zacconding
 * @Date 2018-01-14
 * @GitHub : https://github.com/zacscoding
 */
public class CustomPrinter {

    private static Gson TO_SRING_GSON;
    public static final PrintStream PS = System.out;

    static {
        TO_SRING_GSON = new GsonBuilder().serializeNulls().create();
    }

    public static void print(String content) {
        PS.print(content);
    }

    public static void print(String content, Object... args) {
        PS.print(parseContent(content, args));
    }

    public static void println(String content) {
        PS.println(content);
    }

    public static void println(String content, Object... args) {
        PS.println(parseContent(content, args));
    }

    public static String toJSON(Object inst) {
        return TO_SRING_GSON.toJson(inst);
    }

    private static String parseContent(String content, Object[] args) {
        if (args == null || args.length == 0) {
            return content;
        }

        StringBuilder sb = new StringBuilder();
        int argIdx = 0;
        int length = content.length();

        for (int i = 0; i < length; i++) {
            char curChar = content.charAt(i);
            if ((content.charAt(i) == '{') && (i + 1 < length) && (content.charAt(i + 1) == '}') && (isRange(args, argIdx))) {
                sb.append(args[argIdx++]);
                i++;
            } else {
                sb.append(curChar);
            }
        }

        return sb.toString();
    }

    private static boolean isRange(Object[] array, int idx) {
        if (idx < 0 || array.length <= idx) {
            return false;
        }
        return true;
    }
}

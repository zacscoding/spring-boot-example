package demo.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleLogger for dev
 *
 * @author zacconding
 * @Date 2018-05-31
 * @GitHub : https://github.com/zacscoding
 */
public class SimpleLogger {

    /**
     * Statics
     */
    private static String NEW_LINE;
    private static PrintStream PS;
    private static SimpleDateFormat SIMPLE_DATE_FORMAT;

    static {
        NEW_LINE = System.getProperty("line.separator");
        if (NEW_LINE == null || NEW_LINE.length() == 0) {
            NEW_LINE = "\n";
        }
        SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyMMdd HH:mm:ss.SSS");
        // can change PrintStream here
        PS = System.out;
    }

    public static void setNewLine(String newLine) {
        NEW_LINE = newLine;
    }

    public static void setPS(PrintStream PS) {
        SimpleLogger.PS = PS;
    }

    public static void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        SIMPLE_DATE_FORMAT = simpleDateFormat;
    }

    public static void print(String message, Object... args) {
        StringBuilder sb = new StringBuilder();
        parseContent(sb, message, args);
        PS.print(sb.toString());
    }

    public static void println(String message, Object... args) {
        print(message, args);
        PS.println();
    }

    public static void info(String message, Object... args) {
        String prefix = SIMPLE_DATE_FORMAT.format(new Date()) + " : " + getClassName() + " ";
        println(prefix + message, args);
    }

    public static void error(Throwable t) {
        error(null, t);
    }

    public static void error(String message, Throwable t) {
        String prefix = SIMPLE_DATE_FORMAT.format(new Date()) + " [ERROR] " + getClassName() + " : ";
        println(prefix + (message == null ? "" : message));
        if (t != null) {
            t.printStackTrace(PS);
        }
    }

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

    private static void parseContent(StringBuilder sb, String content, Object[] args) {
        if (args == null || args.length == 0 || content == null || content.length() < 2) {
            sb.append(content);
            return;
        }

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
    }

    private static boolean isRange(Object[] array, int idx) {
        if (idx < 0 || array == null || array.length <= idx) {
            return false;
        }
        return true;
    }

    private static String getClassName() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    private static final class LoggerContextManager {

        private static ThreadLocal<SimpleLogger> contexts = new ThreadLocal<>();

        public static SimpleLogger getOrCreate() {
            SimpleLogger logger = null;

            if ((logger = contexts.get()) == null) {
                logger = new SimpleLogger();
                contexts.set(logger);
            }

            return logger;
        }

        public static SimpleLogger clear() {
            SimpleLogger logger = null;

            if ((logger = contexts.get()) != null) {
                contexts.set(null);
            }

            return logger;
        }
    }


    /**
     * Instance
     */
    private StringBuilder sb;

    private SimpleLogger() {
        sb = new StringBuilder();
    }

    public static SimpleLogger build() {
        return LoggerContextManager.getOrCreate();
    }

    public SimpleLogger append(String message, Object... args) {
        parseContent(this.sb, message, args);
        return this;
    }

    public SimpleLogger appendln(String message, Object... args) {
        parseContent(this.sb, message, args);
        sb.append(NEW_LINE);
        return this;
    }

    public SimpleLogger appendTab(String message, Object... args) {
        appendTab(1, message, args);
        return this;
    }

    public SimpleLogger appendTab(int tabSize, String message, Object... args) {
        parseContent(this.sb, message, args);

        if (tabSize > 0) {
            for (int i = 0; i < tabSize; i++) {
                sb.append('\t');
            }
        }

        return this;
    }

    public SimpleLogger appendRepeat(int repeat, String message, Object... args) {
        if (repeat > 0) {
            for (int i = 0; i < repeat; i++) {
                parseContent(this.sb, message, args);
            }
        }

        return this;
    }

    public SimpleLogger newLine() {
        sb.append(NEW_LINE);
        return this;
    }

    public void flush(PrintStream ps) {
        if (ps != null) {
            ps.print(toString());
        }
    }

    public void flush() {
        flush(System.out);
    }

    @Override
    public String toString() {
        return sb == null ? "" : sb.toString();
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LoggerContextManager.clear();
    }
}
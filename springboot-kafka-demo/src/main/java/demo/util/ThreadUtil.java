package demo.util;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
public class ThreadUtil {

    public static String getThreadInfo() {
        Thread currentThread = Thread.currentThread();
        return currentThread.getName() + "(" + currentThread.getId() + ")";
    }
}

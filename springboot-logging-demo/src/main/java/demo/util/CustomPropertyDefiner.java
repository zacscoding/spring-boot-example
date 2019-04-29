package demo.util;

import ch.qos.logback.core.PropertyDefinerBase;
import java.nio.file.Paths;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class CustomPropertyDefiner extends PropertyDefinerBase {

    private static String JAR_PATH;

    public static String getParentDirectoryFromJar() {
        if (JAR_PATH == null) {
            JAR_PATH = findJarPath();
        }

        return JAR_PATH;
    }

    private static String findJarPath() {
        String dirtyPath = CustomPropertyDefiner.class.getResource("").toString();

        String jarPath = dirtyPath.replaceAll("^.*file:/", ""); //removes file:/ and everything before it
        jarPath = jarPath.replaceAll("jar!.*", "jar"); //removes everything after .jar, if .jar exists in dirtyPath
        jarPath = jarPath.replaceAll("%20", " "); //necessary if path has spaces within

        if (!jarPath
            .endsWith(".jar")) { // this is needed if you plan to run the app using Spring Tools Suit play button.
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }

        String directoryPath = Paths.get(jarPath).getParent().toString(); //Paths - from java 8
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return directoryPath;
        } else {
            return "/" + directoryPath;
        }
    }


    @Override
    public String getPropertyValue() {
        // String fromClass = CustomPropertyDefiner.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        return getParentDirectoryFromJar();
    }
}
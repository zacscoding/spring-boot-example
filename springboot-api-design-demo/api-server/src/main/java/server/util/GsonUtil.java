package server.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.PrintStream;
import java.util.Objects;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
public class GsonUtil {

    public static String toString(Object inst) {
        if (inst == null) {
            return "null";
        }

        return GsonFactory.createDefaultGson().toJson(inst);
    }

    public static String toStringPretty(Object inst) {
        if (inst == null) {
            return "{}";
        }

        return GsonFactory.createPrettyGson().toJson(inst);
    }

    public static void printGson(Object inst) {
        printGson(null, inst);
    }

    public static void printGson(PrintStream ps, Object inst) {
        if (ps == null) {
            ps = System.out;
        }
        ps.println(toString(inst));
    }

    public static void printGsonPretty(Object inst) {
        printGsonPretty(null, inst);
    }

    public static void printGsonPretty(PrintStream ps, Object inst) {
        if (ps == null) {
            ps = System.out;
        }

        ps.println(toStringPretty(inst));
    }

    public static void printGsonPrettyWithTitle(String title, Object inst) {
        printGsonPrettyWithTitle(title, inst, null);
    }

    public static void printGsonPrettyWithTitle(String title, Object inst, PrintStream ps) {
        if (ps == null) {
            ps = System.out;
        }

        ps.println(title);
        printGsonPretty(ps, inst);
    }

    public static String jsonStringToPretty(String jsonString) {
        if (jsonString == null || jsonString.length() == 0) {
            return "{}";
        }

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        return GsonFactory.createPrettyGson().toJson(jsonObject);
    }

    public static <T> T clone(Gson gson, T instance) {
        Objects.requireNonNull(gson, "Gson must be not null");

        if (gson == null || instance == null) {
            return null;
        }

        return (T) gson.fromJson(gson.toJson(instance), instance.getClass());
    }


    public static class GsonFactory {

        public static GsonBuilder createDefaultGsonBuilder() {
            return new GsonBuilder().serializeNulls();
        }

        public static GsonBuilder createDefaultGsonBuilderWithNamingPolicy() {
            return new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }

        public static Gson createDefaultGson() {
            return createDefaultGsonBuilder().create();
        }

        public static Gson createGsonWithNamingPolicy() {
            return createDefaultGsonBuilderWithNamingPolicy().create();
        }

        public static Gson createPrettyGson() {
            return createDefaultGsonBuilder().setPrettyPrinting().create();
        }
    }

    private GsonUtil() {
    }
}

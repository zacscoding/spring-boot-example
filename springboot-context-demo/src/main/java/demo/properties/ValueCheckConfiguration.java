package demo.properties;

import demo.util.SimpleLogger;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-09-28
 * @GitHub : https://github.com/zacscoding
 */
@Profile("props")
@Configuration
public class ValueCheckConfiguration {

    // check space
    @Value("${valueproperties.whitespace1}")
    private String noSpace;
    @Value("${valueproperties.whitespace2}")
    private String leftSpace;
    @Value("${valueproperties.whitespace3}")
    private String rightSpace;

    // check default
    @Value("${valueproperties.default_unknown:its default value}")
    private String defaultString;
    @Value("${valueproperties.default_unknown2:4}")
    private int defaultInt;

    // array
    @Value("${valueproperties.listString}")
    private String[] arrayString;
    @Value("${valueproperties.listInt}")
    private int[] arrayInt;

    // SpEL
    @Value("#{valueCheckTestClass.name}")
    private String valueCheckTestClassName;

    @Autowired
    public ValueCheckConfiguration(@Value("${valueproperties.constructor1}") String constructor1) {
        System.out.println("## >> Check constructor");
        SimpleLogger.println("@Value(\"${valueproperties.constructor1}\") : " + constructor1);
        System.out.println("---------------------------------------------------------------");
    }


    @PostConstruct
    private void test() {
        displaySpaces();
        displayDefaultValue();
        displayArray();
        displaySpEL();
    }

    @Bean
    public ValueCheckTestClass valueCheckTestClass() {
        return new ValueCheckTestClass();
    }

    @Bean
    public String checkBean(@Value("${valueproperties.constructor1}") String constructor1) {
        System.out.println("## >> Check bean method");
        SimpleLogger.println("@Value(\"${valueproperties.constructor1}\") : " + constructor1);
        System.out.println("---------------------------------------------------------------");

        return "checkBean:(";
    }

    private void displaySpaces() {
        System.out.println("## >>>> check spaces..");
        SimpleLogger.println("noSpace length : {}->{} | leftSpace length : {}->{} | rightSpace length : {}->{}", noSpace.length(), noSpace.trim().length(),
            leftSpace.length(), leftSpace.trim().length(), rightSpace.length(), rightSpace.trim().length());
        System.out.println("---------------------------------------------------------------");
    }

    private void displayDefaultValue() {
        System.out.println("## >>>> Check default values...");
        SimpleLogger.println("@Value(\"${valueproperties.default_unknown:its default value}\") : {}", defaultString);
        SimpleLogger.println("@Value(\"${valueproperties.default_unknown2:4\") : {}", defaultInt);
        System.out.println("---------------------------------------------------------------");
    }

    private void displayArray() {
        System.out.println("## >>>> Check array values...");
        SimpleLogger
            .println("{} : {}\n{} : {}", "@Value(\"${valueproperties.listString}\")", Arrays.toString(arrayString), "@Value(\"${valueproperties.listInt}\")",
                Arrays.toString(arrayInt));
        System.out.println("---------------------------------------------------------------");
    }

    private void displaySpEL() {
        SimpleLogger.println("{} : {}", "@Value(\"#{valueCheckTestClass.name}\")", valueCheckTestClassName);
    }

    public static class ValueCheckTestClass {

        public String getName() {
            return "hivava";
        }
    }
}

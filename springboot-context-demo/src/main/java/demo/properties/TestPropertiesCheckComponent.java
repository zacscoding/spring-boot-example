package demo.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-27
 * @GitHub : https://github.com/zacscoding
 */
@Profile("props")
@Component
public class TestPropertiesCheckComponent {

    @Autowired
    private TestPropertiesCheckComponent(TestProperties testProperties) {
        System.out.println("## Check test properties.. :: " + testProperties);
    }
}

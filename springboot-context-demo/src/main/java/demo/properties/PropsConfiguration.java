package demo.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-09-27
 * @GitHub : https://github.com/zacscoding
 */
@Profile("props")
@EnableConfigurationProperties(TestProperties.class)
public class PropsConfiguration {

}

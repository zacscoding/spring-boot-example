package demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-27
 * @GitHub : https://github.com/zacscoding
 */
@Component
@ConfigurationProperties(prefix = "testproperties")
//@Scope("prototype")
public class TestProperties {

    private long timeout;
    private String name;

    public TestProperties() {
        System.out.println("## TestProperties() is called..");
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestProperties{" + "timeout=" + timeout + ", name='" + name + '\'' + '}';
    }
}

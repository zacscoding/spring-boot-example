package demo.learn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * https://www.baeldung.com/hbase
 */
public class HBaseBasicTests {

    @Test
    public void runTests() throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        configuration.addResource(new ClassPathResource("hbase-site.xml").getInputStream());
        HBaseAdmin.available(configuration);
    }
}

package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnTest {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    public void connTest() {

        System.out.println(connectionFactory);
    }

}

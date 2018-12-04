package demo.client;

import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
public class WSConnectionMaintainerTest {

    WebSocketConnectionMaintainer maintainer;

    @Before
    public void setUp() {
        maintainer = new DefaultWebSocketConnectionMaintainer();
    }

    @Test
    public void connect() {

    }
}

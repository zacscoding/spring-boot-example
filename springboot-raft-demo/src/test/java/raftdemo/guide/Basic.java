package raftdemo.guide;

import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.util.Endpoint;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class Basic {

    @Test
    public void temp() {
        Endpoint addr = new Endpoint("localhost", 8080);
        System.out.println(addr);

        PeerId peer1 = new PeerId("localhost", 8080);
        Endpoint peer1Endpoint = peer1.getEndpoint();

        String s = peer1.toString();
        System.out.println(s);

        Configuration conf = JRaftUtils.getConfiguration("localhost:8081,localhost:8082,localhost:8083");
        System.out.println(conf.getPeers().size());
    }
}

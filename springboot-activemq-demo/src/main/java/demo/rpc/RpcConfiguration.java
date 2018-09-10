package demo.rpc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author zacconding
 * @Date 2018-09-10
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc")
@Configuration
@EnableJms
public class RpcConfiguration {



}

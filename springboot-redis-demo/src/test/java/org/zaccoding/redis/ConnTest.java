package org.zaccoding.redis;

import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author zacconding
 * @Date 2018-01-20
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test() {
        System.out.println(redisTemplate);
        List<RedisClientInfo> lists = redisTemplate.getClientList();
        redisTemplate.opsForList().rightPop("key");
        System.out.println("## lists size : " + lists.size());
    }

}

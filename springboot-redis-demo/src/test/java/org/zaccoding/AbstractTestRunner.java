package org.zaccoding;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;

/**
 * @author zacconding
 * @Date 2018-01-21
 * @GitHub : https://github.com/zacscoding
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbstractTestRunner {
    @Autowired
    protected StringRedisTemplate redisTemplate;

    @After
    public void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}

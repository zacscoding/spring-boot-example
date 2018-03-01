package org.zaccoding.document;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.zaccoding.AbstractTestRunner;

/**
 * @author zacconding
 * @Date 2018-01-28
 * @GitHub : https://github.com/zacscoding
 */
public class StringsTest extends AbstractTestRunner {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void setAndGet() {
        System.out.println(redisTemplate.getConnectionFactory().getConnection());
    }




}

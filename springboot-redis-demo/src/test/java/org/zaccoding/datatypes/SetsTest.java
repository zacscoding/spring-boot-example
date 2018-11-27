package org.zaccoding.datatypes;

import org.junit.Test;
import org.springframework.data.redis.core.SetOperations;
import org.zaccoding.AbstractTestRunner;

import javax.annotation.Resource;
import javax.annotation.Resources;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
public class SetsTest extends AbstractTestRunner {

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    @Test
    public void setUp() {
        assertTrue(setOperations.add("zaccoding", "redis", "mongodb", "reditmq", "reditmq") == 3L);
        Set<String> set = setOperations.members("zaccoding");
        System.out.println("## set class : " + set.getClass().getName());
        assertTrue(set.size() == 3);
    }


}

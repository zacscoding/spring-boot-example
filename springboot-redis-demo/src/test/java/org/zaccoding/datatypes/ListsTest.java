package org.zaccoding.datatypes;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.ListOperations;
import org.zaccoding.AbstractTestRunner;
import org.zaccoding.util.CustomPrinter;

import javax.annotation.Resource;
import javax.annotation.Resources;

import static org.junit.Assert.assertTrue;

/**
 * @author zacconding
 * @Date 2018-01-21
 * @GitHub : https://github.com/zacscoding
 */
public class ListsTest extends AbstractTestRunner {

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    @Test
    public void test() {
        listOperations.rightPush("zac", "coding1");
        listOperations.rightPush("zac", "coding2");
        listOperations.rightPush("zac", "coding3");
        listOperations.leftPush("zac", "coding0");
        listOperations.range("zac", 0, -1).forEach(v -> {
            CustomPrinter.println("value : " + v);
        });
    }

    @Test
    public void testBulk() {
        System.out.println(listOperations.rightPushAll("zac", "codding1", "coding2"));
        //assertTrue(listOperations.rightPush("zac", "codding1", "coding2") == 2L);
        listOperations.range("zac", 0, -1).forEach(v -> {
            CustomPrinter.println("value : " + v);
        });
    }
}

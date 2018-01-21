package org.zaccoding.datatypes;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.HashOperations;
import org.zaccoding.AbstractTestRunner;
import org.zaccoding.util.CustomPrinter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zacconding
 * @Date 2018-01-21
 * @GitHub : https://github.com/zacscoding
 */
public class HashesTest extends AbstractTestRunner {
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Test
    public void test() {
        Map<String,String> map = new HashMap<>();
        map.put("name", "zac");
        map.put("hobby", "codding");
        hashOperations.putAll("user", map);
        hashOperations.entries("user").forEach((k,v) -> {
            CustomPrinter.println("key : {}, value : {}", k ,v);
        });
    }
}

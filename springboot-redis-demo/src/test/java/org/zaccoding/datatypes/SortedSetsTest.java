package org.zaccoding.datatypes;

import org.junit.Test;
import org.springframework.data.redis.core.ZSetOperations;
import org.zaccoding.AbstractTestRunner;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
public class SortedSetsTest extends AbstractTestRunner {

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Test
    public void test() {
        String key = "zaccoding";
        zSetOperations.add(key, "redis", 0);
        zSetOperations.add(key, "mongodb", 0);
        zSetOperations.add(key, "reditmq", 0);
        zSetOperations.add(key, "reditmq", 0);

        Set<String> result = zSetOperations.rangeByScore(key, 0, 1000);
        Iterator<String> itr = result.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}

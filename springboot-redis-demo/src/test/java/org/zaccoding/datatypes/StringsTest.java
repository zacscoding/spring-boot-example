package org.zaccoding.datatypes;

import org.junit.Test;
import org.springframework.data.redis.core.ValueOperations;
import org.zaccoding.AbstractTestRunner;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author zacconding
 * @Date 2018-01-21
 * @GitHub : https://github.com/zacscoding
 */
public class StringsTest extends AbstractTestRunner {
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Test
    public void setAndGet() {
        valueOperations.set("name", "zaccoding");
        assertThat(valueOperations.get("name"),is("zaccoding"));
    }


}

package org.zaccoding.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.springframework.data.redis.core.ValueOperations;
import org.zaccoding.AbstractTestRunner;

import javax.annotation.Resource;
import org.zaccoding.util.CollectionUtil;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author zacconding
 * @Date 2018-01-21
 * @GitHub : https://github.com/zacscoding
 */
public class StringsTest extends AbstractTestRunner {

    private static final String key = "zac";
    private static final String value = "coding";

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Override
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void setAndGet() {
        valueOperations.set(key, value);
        assertThat(valueOperations.get(key), is(value));
    }

    @Test
    public void getRange() {
        valueOperations.set(key, value);
        assertThat(valueOperations.getAndSet(key, "newValue"), is(value));
        assertThat(valueOperations.get(key), is("newValue"));
    }

    @Test
    public void multiGetAndSet() {
        // given
        Map<String, String> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String key = "key" + i;
            String value = "value" + i;
            map.put(key, value);
            keys.add(key);
            values.add(value);
        }
        // when
        valueOperations.multiSet(map);
        List<String> findValues = valueOperations.multiGet(keys);

        // then
        assertTrue(CollectionUtil.equalsList(values, findValues));
    }

    @Test
    public void expire() {
        long expireTime = 5000;
        // when
        valueOperations.set(key, value, expireTime, TimeUnit.MILLISECONDS);
        assertThat(valueOperations.get(key), is(value));
        System.out.println("## success to save");
        try {
            Thread.sleep(expireTime);
        } catch (Exception e) {
            fail("FAIL!!!");
        }
        // then
        assertNull(valueOperations.get(key));
    }
}

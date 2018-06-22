package org.bootdemo;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bootdemo.web.IndexController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterAdviceTest {

    @Autowired
    IndexController indexController;

    @Test
    public void validParamMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("PAGE_NO", "10");
        param.put("PAGE_SIZE", "11");
        indexController.annoRequest(param);
    }

    @Test
    public void invalidParamMap() {
        Map<String, Object> param = new HashMap<>();
        try {
            indexController.annoRequest(param);
            fail();
        } catch (Exception e) {
        }
        param.clear();

        param.put("PAGE_NO", "a");
        try {
            indexController.annoRequest(param);
            fail();
        } catch (Exception e) {
        }
        param.clear();
    }
}

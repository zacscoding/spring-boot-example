package demo.dynamic.route;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicRouteLocatorTests {

    @Autowired
    DynamicRouteLocator dynamicRouteLocator;

    @Test
    public void test_changeUrl() {
        URL initUrl = dynamicRouteLocator.getCurrentUrl();
        dynamicRouteLocator.changeUrl();
        URL nextUrl = dynamicRouteLocator.getCurrentUrl();

        if (dynamicRouteLocator.getUrls().length == 1) {
            assertThat(initUrl).isEqualTo(nextUrl);
        } else {
            assertThat(initUrl).isNotEqualTo(nextUrl);
        }
    }
}

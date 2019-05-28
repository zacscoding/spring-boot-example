package demo.dynamic;

import static org.assertj.core.api.Java6Assertions.assertThat;

import demo.dynamic.route.DynamicRouteLocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class DynamicRouteTests {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    DynamicRouteLocator dynamicRouteLocator;


    @Test
    public void test_callDynamicAndFooResponse() {
        // when
        String body = restTemplate.getForObject("/dynamic", String.class);

        // then
        assertThat(body).isEqualTo("Foo");
    }

    @Test
    public void test_callDynamicAfterChangeEndpointUrl() {
        // given
        String body = restTemplate.getForObject("/dynamic", String.class);
        assertThat(body).isEqualTo("Foo");

        // when then
        String changedUrl = restTemplate.getForObject("/dynamic-config/change", String.class);
        assertThat(changedUrl).isEqualTo(dynamicRouteLocator.getCurrentUrl().toExternalForm());

        body = restTemplate.getForObject("/dynamic", String.class);
        assertThat(body).isEqualTo("Bar");
    }
}

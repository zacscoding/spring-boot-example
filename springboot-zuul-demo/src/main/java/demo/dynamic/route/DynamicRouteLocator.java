package demo.dynamic.route;

import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class DynamicRouteLocator {

    private URL[] urls;

    @PostConstruct
    void setUp() {
        try {
            urls = new URL[]{
                new URL("http://localhost:8081/dynamic-foo"),
                new URL("http://localhost:8081/dynamic-bar")
            };
        } catch (MalformedURLException e) {
            logger.warn("invalid url.", e);
            throw new RuntimeException(e);
        }
    }

    private int urlIdx = 0;

    public URL getCurrentUrl() {
        return urls[urlIdx];
    }

    public void changeUrl() {
        String beforeUrl = urls[urlIdx].toExternalForm();
        urlIdx = (urlIdx + 1) % urls.length;
        String afterUrl = urls[urlIdx].toExternalForm();
        logger.info("Change to url {} --> {}", beforeUrl, afterUrl);
    }

    // for tests
    URL[] getUrls() {
        return urls;
    }
}
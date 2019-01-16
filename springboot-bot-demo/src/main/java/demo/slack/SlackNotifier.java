package demo.slack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author zacconding
 * @Date 2019-01-16
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class SlackNotifier {

    private RestTemplate restTemplate;

    @Autowired
    public SlackNotifier(RestTemplate restTemplate) {
        
    }
}

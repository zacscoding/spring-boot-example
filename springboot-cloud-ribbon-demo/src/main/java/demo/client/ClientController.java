package demo.client;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ClientController {

    private final RestTemplate restTemplate;

    @PostConstruct
    public void setUp() {
        System.out.println("## Check rest template");
        System.out.println(restTemplate.getClass().getName());

    }

    @GetMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "Artaban") String name) {
        String greeting = this.restTemplate.getForObject("http://say-hello/greeting", String.class);
        return String.format("%s, %s!", greeting, name);
    }
}

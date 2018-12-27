package demo;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/alive")
    public boolean isAlive() {
        try {
            return restHighLevelClient.ping();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

package demo.web;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-09-05
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequestMapping("/okhttp/**")
@RestController
public class OkHttpClientTestController {

    @GetMapping("/date")
    public String getDate() {
        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/async/date")
    public DeferredResult<String> getDateAsync() {
        log.info("Request /async/date");
        final DeferredResult<String> result = new DeferredResult<>();
        Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
                log.info("> Set result");
                result.setResult(String.valueOf(System.currentTimeMillis()));
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        });
        t.start();
        return result;
    }

    @GetMapping("/ex/bars")
    public String getQueryParam(@RequestParam(name = "id", required = false) String id) {
        log.info(">> /ex/bars. id : {}", id);
        return id;
    }

    @PostMapping("/users")
    public String postUsers(@RequestBody String body) {
        log.info(">> /users. body : {}", body);
        return body;
    }

    @PostMapping("/auth")
    public String postAuth(@RequestBody String body) {
        log.info(">> /auth. body : {}");
        return body;
    }

    @GetMapping("/test-auth")
    public String testAuth() {
        log.info(">> /test. body : {}");
        return "success";
    }
}
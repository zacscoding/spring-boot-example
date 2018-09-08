package demo.web;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import demo.util.ServletUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

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
        log.info("## > /date.");
        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/async/date")
    public DeferredResult<String> getDateAsync() {
        log.info("## /async/date");
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
        log.info("## >> /ex/bars. id : {}", id);
        return id;
    }

    @PostMapping("/users")
    public String postUsers(@RequestBody String body) {
        log.info("## >> /users. body : {}", body);
        return body;
    }

    @PostMapping("/details")
    public String postUserDetails(@RequestBody String datails) {
        log.info("## >> /details. body : {}", datails);
        return "SUCCESS";
    }

    @PostMapping("/multipart")
    public String postMultipart(@RequestParam("username") String username, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info("## >> /multipart. username : {} file name : {}", username, multipartFile.getOriginalFilename());
        log.info("## >> file content : \n" + new String(multipartFile.getBytes()));
        return "SUCCESS";
    }

    /*@RequestMapping(value = "/auth", method = {RequestMethod.GET, RequestMethod.POST})
    public String postAuth(@RequestBody(required = false) String body) {
        log.info(">> /auth. body : {}", body);
        return StringUtils.hasLength(body) ? body : "EMPTY";
    }*/

    @GetMapping("/defaultHeader")
    public String postDefaultHeader() {
        log.info("## >> /defaultHeader. Content-Type : {}", ServletUtil.getHeader("Content-Type"));
        return "SUCESS";
    }

    @GetMapping("/delay/{timeout}")
    public String delay(@PathVariable("timeout") long timeout) throws InterruptedException {
        log.info("## > /delay/{}", timeout);
        TimeUnit.SECONDS.sleep(timeout);
        return "SUCCESS";
    }

}
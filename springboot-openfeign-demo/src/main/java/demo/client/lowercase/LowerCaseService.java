package demo.client.lowercase;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import demo.common.MessageRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface LowerCaseService {

    @RequestLine("POST /lowercase/message")
    @Headers({
            "Content-Type: application/json",
            "Service-Target: {groupName}"
    })
    String call(@Param("groupName") String groupName, @RequestBody MessageRequest request);
}

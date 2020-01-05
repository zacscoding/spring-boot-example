package demo.client.uppercase;

import demo.common.MessageRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface UpperCaseService {

    @RequestLine("POST /uppercase/message")
    @Headers({
            "Content-Type: application/json",
            "Service-Target: {groupName}"
    })
    String call(@Param("groupName") String groupName, MessageRequest request);
}

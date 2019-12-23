package demo.feign;

import demo.echo.EchoRequest;
import demo.echo.EchoResponse;
import feign.Headers;
import feign.RequestLine;

public interface EchoHandler {
    @RequestLine("POST /")
    @Headers("Content-Type: application/json")
    EchoResponse call(EchoRequest request);
}

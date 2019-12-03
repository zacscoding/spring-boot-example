# Best practice about http client.  

## TODO 

- [x] Determine route in RestTemplate
- [ ] Client side load balancer with Ribbon

---



> ## Start servers  

; Server have only two endpoints.

1. "/data" : returns UUID random string
2. "/alive" : returns 200 status code with empty body


```aidl
// start a server1 with 8080 port
$ ./gradlew bootRun  -PjvmArgs="-Dserver.port=8081"

// start a server2 with 8081 port
$ ./gradlew bootRun -PjvmArgs="-Dserver.port=8082"

// start a server3 with 8082 port
$ ./gradlew bootRun -PjvmArgs="-Dserver.port=8083"
```  

> ## Run RestTemplate examples  

<a href="src/test/java/demo/resttemplate/RestTemplateProxyTest.java">**See tests with rest template**</a>  

```
$ ./gradlew test --tests RestTemplateProxyTest
```

> ## RestTemplate with proxy route planner  

```aidl
    // create a new RestTemplate instance with proxy configuration
    private RestTemplate build() {
        final HttpHost proxy = new HttpHost("proxy.example.com");
        final HttpClient httpClient
                = HttpClientBuilder.create()
                                   .setRoutePlanner(new DefaultProxyRoutePlanner(proxy) {

                                       @Override
                                       public HttpHost determineProxy(HttpHost target,
                                                                      HttpRequest request,
                                                                      HttpContext context) {

                                           return getActiveHost().orElse(hosts[0]);
                                       }
                                   }).build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    // returns active http host
    private Optional<HttpHost> getActiveHost() {
        final int start = atomicInteger.getAndIncrement();

        for (int i = 0; i < hosts.length; i++) {
            final int idx = (start + i) % hosts.length;
            final HttpHost targetHost = hosts[idx];

            CloseableHttpResponse response = null;
            try {
                HttpGet request = new HttpGet(targetHost.toURI().concat("/alive"));

                response = httpClient.execute(request);

                if (response.getStatusLine().getStatusCode() == 200) {
                    return Optional.of(targetHost);
                }
            } catch (IOException e) {
                // ignore
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return Optional.empty();
    }
```

---  

## References  

- https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-http-clients-proxy-configuration
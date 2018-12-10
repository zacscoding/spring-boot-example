package client.api.v1.person;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author zacconding
 * @Date 2018-12-04
 * @GitHub : https://github.com/zacscoding
 */
public class PersonApiUrlV1 {

    public static URI getPersonURI(String id) {
        return createDefaultBuilder().pathSegment("{id}").build(id);
    }

    public static URI deletePersonURI(String id) {
        return createDefaultBuilder().pathSegment("{id}").build(id);
    }

    public static URI getPersonAllURI() {
        return createDefaultBuilder().path("/all").build().toUri();
    }

    public static URI savePersonURI() {
        return createDefaultBuilder().path("/save").build().toUri();
    }

    private static UriComponentsBuilder createDefaultBuilder() {
        return UriComponentsBuilder.fromHttpUrl("http://127.0.0.1:8989").pathSegment("person", "v1");
    }
}

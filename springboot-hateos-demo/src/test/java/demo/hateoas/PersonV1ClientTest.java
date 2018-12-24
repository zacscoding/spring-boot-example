package demo.hateoas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.client.Hop.rel;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.SelfDescribing;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author zacconding
 * @Date 2018-12-25
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonV1ClientTest {

    @LocalServerPort
    int port;
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getPersonAndThenDiscoverer() throws Exception {
        // given
        String response = restTemplate.getForObject(new URI("http://localhost:" + port + "/api/v1/person/1"), String.class);
        JsonNode linkNode = objectMapper.readTree(response).get("_links");
        String linksJson = "{ \"_links\" : " + linkNode.toString() + "  }";

//        .andExpect(jsonPath("_links.self").exists())
//            .andExpect(jsonPath("_links.update").exists())
//            .andExpect(jsonPath("_links.delete").exists());
        LinkDiscoverer discoverer = new HalLinkDiscoverer();
        Link selfLink = discoverer.findLinkWithRel("self", linksJson);

        assertThat(selfLink.getRel()).isEqualTo("self");
        assertThat(selfLink.getHref()).isEqualTo("http://localhost:" + port + "/api/v1/person/1");

        Link updateLink = discoverer.findLinkWithRel("update", linksJson);
        assertThat(updateLink.getRel()).isEqualTo("update");
        assertThat(updateLink.getHref()).isEqualTo("http://localhost:" + port + "/api/v1/person/update/1");

        Link deleteLink = discoverer.findLinkWithRel("delete", linksJson);
        assertThat(deleteLink.getRel()).isEqualTo("delete");
        assertThat(deleteLink.getHref()).isEqualTo("http://localhost:" + port + "/api/v1/person/1");
    }
}

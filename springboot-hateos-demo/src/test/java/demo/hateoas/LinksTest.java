package demo.hateoas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.hateoas.Link;

/**
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#fundamentals.obtaining-links.entity-links
 */
public class LinksTest {

    @Test
    public void basic() {
        String url1 = "http://localhost:8080/somthing";
        Link link = new Link(url1);
        assertThat(link.getHref()).isEqualTo(url1);
        assertThat(link.getRel()).isEqualTo(Link.REL_SELF);

        String rel = "my-rel";
        Link link2 = new Link(url1, rel);
        assertThat(link2.getHref()).isEqualTo(url1);
        assertThat(link2.getRel()).isEqualTo(rel);
    }


}

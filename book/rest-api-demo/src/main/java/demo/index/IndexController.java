package demo.index;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import demo.events.EventController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
@RestController
public class IndexController {

    @GetMapping("/api")
    public ResourceSupport index() {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}

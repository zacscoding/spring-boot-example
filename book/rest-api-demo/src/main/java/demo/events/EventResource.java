package demo.events;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring Resource의 getContent()에 @JsonUnwrapped 되어있어서
 * Resource로 대체
 */
//public class EventResource extends ResourceSupport {
public class EventResource extends Resource<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}

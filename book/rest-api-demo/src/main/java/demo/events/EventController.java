package demo.events;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import demo.accounts.Account;
import demo.accounts.AccountAdapter;
import demo.accounts.CurrentUser;
import demo.common.ErrorsResource;
import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.plugin.liveconnect.SecurityContextHelper;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private EventRepository eventRepository;
    private ModelMapper modelMapper;
    private EventValidator eventValidator;

    @Autowired
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {

        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors,
        @CurrentUser Account account) {

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setManager(account);

        Event newEvent = eventRepository.save(event);
        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash("{id}");
        URI createdUri = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(newEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(linkTo(EventController.class).withRel("update-event"));
        eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler,
        @CurrentUser Account account) {

        Page<Event> page = this.eventRepository.findAll(pageable);

        PagedResources<Resource<Event>> pagedResourcesAssembler
            = assembler.toResource(page, e -> new EventResource(e));

        pagedResourcesAssembler.add(
            new Link("/docs/index.html#resources-events-list").withRel("profile")
        );

        // create link
        if (account != null) {
            pagedResourcesAssembler.add(linkTo(EventController.class).withRel("create-events"));
        }

        return ResponseEntity.ok(pagedResourcesAssembler);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getEvent(@PathVariable("id") Integer id, @CurrentUser Account account) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();

        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#get-an-event").withRel("profile"));

        if (event.getManager().equals(account)) {
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(eventResource);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateEvent(@PathVariable("id") Integer id, @RequestBody @Valid EventDto eventDto,
        @CurrentUser Account account, Errors errors) {

        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();

        if (!existingEvent.getManager().equals(account)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        modelMapper.map(eventDto, existingEvent);
        existingEvent.update();

        Event savedEvent = eventRepository.save(existingEvent);
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("/docs/index.html#resources-update-event").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}

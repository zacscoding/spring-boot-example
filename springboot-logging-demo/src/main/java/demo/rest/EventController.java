package demo.rest;

import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import demo.domain.Event;
import demo.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    @GetMapping("/event/{id}")
    public ResponseEntity getEvent(@PathVariable("id") long id) {
        logger.info("/event/{} is called with tx id : {}"
                , id, MDC.get("txId"));

        final Optional<Event> eventOptional = eventService.findById(id);

        if (!eventOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(eventOptional.get());
    }
}

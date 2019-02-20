package statemachine.controller;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import statemachine.orm.ComponentEntity;
import statemachine.state.component.ComponentEvent;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/state")
public class StatemachineApiController {

    @GetMapping("/")
    public Map<String, Object> getStates() {
        return ImmutableMap.<String, Object>builder()
            .put("component", Collections.emptyList())
            .build();
    }

    @GetMapping("/component")
    public Map<String, Object> getComponents() {
        return ImmutableMap.<String, Object>builder()
            .put("component", Collections.emptyList())
            .build();
    }

    @PostMapping("/component/create")
    public String createComponent(@RequestBody ComponentEntity componentEntity) {
        return "NOT IMPL";
    }

    @PutMapping("/component/{id}/update/{event}")
    public String receiveEvent(@PathVariable("id") Long id, @PathVariable("event") String eventName) {
        logger.info("Receive event.. id : {} / event : {}", id, eventName);

        ComponentEvent event = ComponentEvent.getType(eventName);

        if (event == ComponentEvent.UNKNOWN) {
            return "Unknown component event " + eventName;
        }

        /*
        try {
            // TODO :: handle event
        } catch (InvalidTransactionException e) {
            logger.warn("Invalid transition event occur", e);
            return e.getMessage();
        }
        */

        return "Not impl yet";
    }
}

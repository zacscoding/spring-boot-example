package statemachine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import statemachine.state.component.HostComponentEntity;
import statemachine.state.component.HostComponentEvent;
import statemachine.state.component.HostComponentService;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/state")
public class StatemachineApiController {

    private HostComponentService hostComponentService;
    private ObjectMapper objectMapper;

    @Autowired
    public StatemachineApiController(HostComponentService hostComponentService, ObjectMapper objectMapper) {
        this.hostComponentService = hostComponentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String getStates() {
        return toJsonPrettyString(ImmutableMap.<String, Object>builder()
            .put("component", hostComponentService.getHostComponents())
            .build()
        );
    }

    @GetMapping("/component")
    public String getComponents() {
        return toJsonPrettyString(
            ImmutableMap.<String, Object>builder()
                .put("component", hostComponentService.getHostComponents())
                .build()
        );
    }

    @PostMapping("/component/create")
    public String createComponent(@RequestBody HostComponentEntity componentEntity) {
        return toJsonPrettyString(hostComponentService.saveHostComponent(componentEntity));
    }

    @PutMapping("/component/{id}/update/{event}")
    public String receiveEvent(@PathVariable("id") Long id, @PathVariable("event") String eventName) {
        logger.info("Receive event.. id : {} / event : {}", id, eventName);

        HostComponentEvent event = HostComponentEvent.getType(eventName);

        if (event == HostComponentEvent.UNKNOWN) {
            return "Unknown component event " + eventName;
        }

        return "" + hostComponentService.handleEvent(id, event);
    }

    private String toJsonPrettyString(Object object) {
        if (object == null) {
            return "null";
        }

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
    }
}

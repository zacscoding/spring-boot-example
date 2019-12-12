package demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import demo.domain.Event;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@Service
public class EventService {

    private List<Event> events = new ArrayList<>();

    @PostConstruct
    private void setUp() {
        final LocalDateTime now = LocalDateTime.now();

        for (long id = 1L; id <= 5L; id += 1L) {
            events.add(
                    Event.builder()
                         .id(id)
                         .title("event" + id)
                         .description("dummy event.. " + id)
                         .startAt(now.plusDays(id))
                         .build()

            );
        }
    }

    public Optional<Event> findById(final Long id) {
        int sleep = new Random().nextInt(5) + 1;
        logger.info("Working find by id with sleep {} sec ==> id : {}, tx id : {}",
                    sleep, id, MDC.get("txId"));
        try {
            TimeUnit.SECONDS.sleep(sleep);
        } catch (Exception e) {
        }

        Optional<Event> eventOptional = events.stream().filter(e -> e.getId() == id).findFirst();
        logger.info("will return(isPresent == {}) from {}"
                , eventOptional.isPresent(), MDC.get("txId"));

        return events.stream().filter(e -> e.getId() == id).findFirst();
    }
}

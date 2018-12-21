package demo.events;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
            .name("Inflearn Spring Rest API")
            .description("REST API development with Spring")
            .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        String description = "Spring";
        String name = "Event";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription("Spring");

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}
package demo.events;


import static org.assertj.core.api.Assertions.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
@RunWith(JUnitParamsRunner.class)
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

    @Test
//    @Parameters({
//        "0, 0, true",
//        "100, 0, false",
//        "0, 100, false"
//    })
    @Parameters(method = "parametersForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
            .basePrice(basePrice)
            .maxPrice(maxPrice)
            .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    // convention
    private Object[] parametersForTestFree() {
        return new Object[]{
            new Object[]{0, 0, true}
            , new Object[]{100, 0, false}
            , new Object[]{0, 100, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
            .location(location)
            .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline() {
        return new Object[]{
            new Object[]{"강남역 네이버 D2 스타텁 팩토리", false}
            , new Object[]{null, true}
        };
    }
}
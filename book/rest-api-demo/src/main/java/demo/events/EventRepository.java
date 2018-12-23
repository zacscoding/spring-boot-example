package demo.events;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
public interface EventRepository extends JpaRepository<Event, Integer> {
}

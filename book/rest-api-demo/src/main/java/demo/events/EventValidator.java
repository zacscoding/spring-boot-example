package demo.events;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if ((eventDto.getBasePrice() > eventDto.getMaxPrice()) && (eventDto.getMaxPrice() > 0)) {
            errors.reject("wrongPrice", "Values fo prices are wrong");
            // errors.rejectValue("basePrice", "wrongValue");
            // errors.rejectValue("maxPrice", "wrongValue");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {

            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        // TODO BeginEventDateTime
        // TODO CloseEnrollmentDateTime
    }
}

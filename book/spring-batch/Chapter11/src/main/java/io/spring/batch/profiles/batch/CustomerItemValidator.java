package io.spring.batch.profiles.batch;

import java.util.Collections;
import java.util.Map;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;

import io.spring.batch.profiles.domain.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerItemValidator implements Validator<CustomerUpdate> {
    private static final String FIND_CUSTOMER = "SELECT COUNT(*) FROM customer WHERE customer_id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void validate(CustomerUpdate customer) throws ValidationException {
        final Map<String, Long> parameterMap = Collections.singletonMap("id", customer.getCustomerId());
        final Long count = jdbcTemplate.queryForObject(FIND_CUSTOMER, parameterMap, Long.class);

        if (Objects.equal(count, 0L)) {
            logger.error("Failed to find a customer. customer id {} will be filtered", customer.getCustomerId());
            throw new ValidationException(
                    String.format("Customer id %s was not able to be found", customer.getCustomerId()));
        }
    }
}

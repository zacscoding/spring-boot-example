package io.spring.batch.example5;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import io.spring.batch.domain.Customer;

public class UniqueLastNameValidator extends ItemStreamSupport implements Validator<Customer> {

    private Set<String> lastNames = new HashSet<>();

    @Override
    public void validate(Customer value) throws ValidationException {
        if (lastNames.contains(value.getLastName())) {
            throw new ValidationException("Duplicate last name was found: " + value.getLastName());
        }
        this.lastNames.add(value.getLastName());
    }

    @Override
    public void open(ExecutionContext executionContext) {
        String lastNames = getExecutionContextKey("lastNames");

        if (executionContext.containsKey(lastNames)) {
            this.lastNames = (Set<String>) executionContext.get(lastNames);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
        Iterator<String> iterator = lastNames.iterator();
        Set<String> copiedLastNames = new HashSet<>();
        while (iterator.hasNext()) {
            copiedLastNames.add(iterator.next());
        }

        executionContext.put(getExecutionContextKey("lastNames"), copiedLastNames);
    }
}

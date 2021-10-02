package io.spring.batch.database.example9;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import io.spring.batch.database.domain.Customer;
import io.spring.batch.database.domain.CustomerGenerator;

public class CustomerItemReader extends ItemStreamSupport implements ItemReader<Customer> {

    private List<Customer> customers;
    private int curIndex;
    private String INDEX_KEY = "current.index.customers";

    public CustomerItemReader() {
        customers = IntStream.range(0, 100)
                             .boxed()
                             .map(i -> CustomerGenerator.createCustomer())
                             .collect(Collectors.toList());
        curIndex = 0;
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (curIndex == 50) {
            throw new RuntimeException("This will end your execution");
        }

        if (curIndex < customers.size()) {
            return customers.get(curIndex++);
        }
        return null;
    }

    @Override
    public void close() throws ItemStreamException {}

    @Override
    public void open(ExecutionContext executionContext) {
        if (!executionContext.containsKey(getExecutionContextKey(INDEX_KEY))) {
            curIndex = 0;
            return;
        }

        final int index = executionContext.getInt(getExecutionContextKey(INDEX_KEY));
        if (index == 50) {
            curIndex = 51;
            return;
        }
        curIndex = index;
    }

    @Override
    public void update(ExecutionContext executionContext) {
        executionContext.putInt(getExecutionContextKey(INDEX_KEY), curIndex);
    }
}

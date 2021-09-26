package io.spring.batch.file.flatfile.multi;

import java.util.ArrayList;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import io.spring.batch.file.domain.Customer;
import io.spring.batch.file.domain.Transaction;

/**
 * {@link ItemReader}를 사용할 경우 읽을 대상 리소스를 열고 닫는 것을 제어할 뿐만 아니라 읽어들인 레코드와 관련된 상태를 {@link ExecutionContext}에
 * 관리해야 하므로 {@link ItemStreamReader}를 활용한다.
 */
class CustomerFileReader implements ItemStreamReader<Customer> {

    private Object curItem = null;
    private ItemStreamReader<Object> delegate;

    public CustomerFileReader(ItemStreamReader<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (curItem == null) {
            curItem = delegate.read();
        }

        final Customer item = (Customer) curItem;
        curItem = null;

        if (item != null) {
            item.setTransactions(new ArrayList<>());

            while (peek() instanceof Transaction) {
                item.getTransactions().add((Transaction) curItem);
                curItem = null;
            }
        }

        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

    private Object peek() throws Exception {
        if (curItem == null) {
            curItem = delegate.read();
        }
        return curItem;
    }
}

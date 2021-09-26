package io.spring.batch.file.domain;

import java.util.Date;

import com.google.common.base.MoreObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    private String accountNumber;
    private Date transactionDate;
    private Double amount;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("accountNumber", accountNumber)
                          .add("transactionDate", transactionDate)
                          .add("amount", amount)
                          .toString();
    }
}

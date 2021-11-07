package io.spring.batch.profiles.batch;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.batch.item.file.transform.LineAggregator;

import io.spring.batch.profiles.domain.Account;
import io.spring.batch.profiles.domain.Customer;
import io.spring.batch.profiles.domain.Statement;
import io.spring.batch.profiles.domain.Transaction;

public class StatementLineAggregator implements LineAggregator<Statement> {

    private static final String ADDRESS_LINE_ONE = String.format("%121s\n", "Apress Banking");
    private static final String ADDRESS_LINE_TWO = String.format("%120s\n", "1060 West Addison St.");
    private static final String ADDRESS_LINE_THREE = String.format("%120s\n\n", "Chicago, IL 60613");
    private static final String STATEMENT_DATE_LINE = String.format("Your Account Summary %78s ", "Statement Period")
                                                      + "%tD to %tD\n\n";

    public String aggregate(Statement statement) {
        StringBuilder output = new StringBuilder();

        formatHeader(statement, output);
        formatAccount(statement, output);

        return output.toString();
    }

    private void formatAccount(Statement statement, StringBuilder output) {
        for (Account account : statement.getAccounts()) {
            output.append(String.format(STATEMENT_DATE_LINE,
                                        account.getLastStatementDate(),
                                        new Date()));

            BigDecimal creditAmount = new BigDecimal(0);
            BigDecimal debitAmount = new BigDecimal(0);
            for (Transaction transaction : account.getTransactions()) {
                if (transaction.getCredit() != null) {
                    creditAmount = creditAmount.add(transaction.getCredit());
                }

                if (transaction.getDebit() != null) {
                    debitAmount = debitAmount.add(transaction.getDebit());
                }

                output.append(String.format("               %tD          %-50s    %8.2f\n",
                                            transaction.getTimestamp(),
                                            transaction.getDescription(),
                                            transaction.getTransactionAmount()));
            }

            output.append(String.format("%80s %14.2f\n", "Total Debit:", debitAmount));
            output.append(String.format("%81s %13.2f\n", "Total Credit:", creditAmount));
            output.append(String.format("%76s %18.2f\n\n", "Balance:", account.getBalance()));
        }
    }

    private void formatHeader(Statement statement, StringBuilder output) {
        final Customer customer = statement.getCustomer();

        final String customerName = String.format("\n%s %s", customer.getFirstName(), customer.getLastName());
        output.append(customerName).append(ADDRESS_LINE_ONE.substring(customerName.length()));

        output.append(customer.getAddress1()).append(ADDRESS_LINE_TWO.substring(customer.getAddress1().length()));

        final String addressString = String.format("%s, %s %s", customer.getCity(), customer.getState(),
                                                   customer.getPostalCode());
        output.append(addressString).append(ADDRESS_LINE_THREE.substring(addressString.length()));
    }
}
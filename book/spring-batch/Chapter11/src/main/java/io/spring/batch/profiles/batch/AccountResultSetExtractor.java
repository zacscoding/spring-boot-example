package io.spring.batch.profiles.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import io.spring.batch.profiles.domain.Account;
import io.spring.batch.profiles.domain.Transaction;

public class AccountResultSetExtractor implements ResultSetExtractor<List<Account>> {

    private final List<Account> accounts = new ArrayList<>();
    private Account curAccount;

    @Nullable
    @Override
    public List<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            if (curAccount == null) {
                curAccount = new Account(
                        rs.getLong("account_id"),
                        rs.getBigDecimal("balance"),
                        rs.getDate("last_statement_date"));
            } else if (rs.getLong("account_id") != curAccount.getId()) {
                accounts.add(curAccount);
                curAccount = new Account(rs.getLong("account_id"),
                                         rs.getBigDecimal("balance"),
                                         rs.getDate("last_statement_date"));
            }

            if (StringUtils.hasText(rs.getString("description"))) {
                curAccount.addTransaction(
                        new Transaction(rs.getLong("transaction_id"),
                                        rs.getLong("account_id"),
                                        rs.getString("description"),
                                        rs.getBigDecimal("credit"),
                                        rs.getBigDecimal("debit"),
                                        new Date(rs.getTimestamp("timestamp").getTime())));
            }
        }

        if (curAccount != null) {
            accounts.add(curAccount);
        }

        return accounts;
    }
}
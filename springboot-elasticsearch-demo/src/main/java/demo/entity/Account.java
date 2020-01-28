package demo.entity;

import java.util.TreeMap;

/**
 *
 */
public class Account {

    /** address */
    private String address;

    /** first block time contains transaction */
    private long foundTime;

    /** balance of address */
    private String balance;

    /** code of this account if smart contract */
    private String code;

    private TreeMap<Long, String> balanceHistory;

    public static Account merge(Account account1, Account account2) {
        long blockNumber1 = account1.balanceHistory.lastKey();

    }
}

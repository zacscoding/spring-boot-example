package demo.domain;

/**
 * Transaction entity interface
 */
public interface Transaction {

    /**
     * Returns a transaction hash
     */
    String getTransactionHash();

    /**
     * Returns this transaction's index started with 0 in block's transactions
     */
    Integer getTransactionIndex();

    /**
     * Returns a block number in this transaction
     */
    Long getBlockNumber();

    /**
     * Returns a block hash in this transaction
     */
    String getBlockHash();
}

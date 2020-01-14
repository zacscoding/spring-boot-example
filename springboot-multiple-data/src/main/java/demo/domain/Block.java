package demo.domain;

import java.util.List;

/**
 * Block entity
 */
public interface Block {

    /**
     * Returns a block number
     */
    Long getBlockNumber();

    /**
     * Returns a block hash
     */
    String getBlockHash();

    /**
     * Returns transaction hash list in this block
     */
    List<String> getTransactionHashes();

    /**
     * Returns count of transactions in this block
     */
    Integer getTransactionCount();
}

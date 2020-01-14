package demo.domain.jpa;

import demo.dto.BlockDto;
import demo.dto.TransactionDto;

/**
 * JPA entity <===> DTO converter
 */
public final class JpaDomainAssembler {

    /**
     * Convert {@link JpaBlock} to {@link BlockDto}
     */
    public static BlockDto convertBlockDto(JpaBlock block) {
        return BlockDto.builder()
                       .id(block.getId())
                       .blockHash(block.getBlockHash())
                       .blockNumber(block.getBlockNumber())
                       .transactionHashes(block.getTransactionHashes())
                       .transactionCount(block.getTransactionCount())
                       .build();
    }

    /**
     * Convert {@link JpaTransaction} to {@link TransactionDto}
     */
    public static TransactionDto convertTransactionDto(JpaTransaction transaction) {
        return TransactionDto.builder()
                             .id(transaction.getId())
                             .transactionHash(transaction.getTransactionHash())
                             .transactionIndex(transaction.getTransactionIndex())
                             .blockHash(transaction.getBlockHash())
                             .blockNumber(transaction.getBlockNumber())
                             .build();
    }

    private JpaDomainAssembler() {
    }
}

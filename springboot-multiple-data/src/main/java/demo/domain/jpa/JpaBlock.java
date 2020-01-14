package demo.domain.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import demo.domain.Block;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@link Block} based on jpa entity
 */
@Entity
@Setter(AccessLevel.PROTECTED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaBlock implements Block {

    @Id
    @GeneratedValue
    @Column(name = "block_id")
    private Long id;

    private Long blockNumber;

    private String blockHash;

    @ElementCollection
    private List<String> transactionHashes = new ArrayList<>();

    private Integer transactionCount;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JpaTransaction> transactions = new ArrayList<>();

    /**
     * Create a new {@link JpaBlock}
     */
    public static JpaBlock create(Long blockNumber, String blockHash, List<JpaTransaction> transactions) {
        JpaBlock block = new JpaBlock();

        block.setBlockNumber(blockNumber);
        block.setBlockHash(blockHash);
        block.setTransactions(transactions);
        block.setTransactionHashes(transactions.stream()
                                               .map(JpaTransaction::getTransactionHash)
                                               .collect(Collectors.toList()));
        block.setTransactionCount(transactions.size());

        for (JpaTransaction transaction : transactions) {
            transaction.setBlock(block);
        }

        return block;
    }

    @Override
    public Long getBlockNumber() {
        return blockNumber;
    }

    @Override
    public String getBlockHash() {
        return blockHash;
    }

    @Override
    public List<String> getTransactionHashes() {
        return transactionHashes;
    }

    @Override
    public Integer getTransactionCount() {
        return transactionHashes.size();
    }
}

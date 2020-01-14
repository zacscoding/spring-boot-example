package demo.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import demo.domain.Transaction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@link Transaction} based on jpa entity
 */
@Entity
@Setter(AccessLevel.PROTECTED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaTransaction implements Transaction {

    @Id
    @GeneratedValue
    @Column(name = "transaction_id")
    private Long id;

    private String transactionHash;

    private Integer transactionIndex;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private JpaBlock block;

    public static JpaTransaction create(String transactionHash, Integer transactionIndex) {
        JpaTransaction transaction = new JpaTransaction();

        transaction.setTransactionHash(transactionHash);
        transaction.setTransactionIndex(transactionIndex);

        return transaction;
    }

    @Override
    public String getTransactionHash() {
        return transactionHash;
    }

    @Override
    public Integer getTransactionIndex() {
        return transactionIndex;
    }

    @Override
    public Long getBlockNumber() {
        return getBlock().getBlockNumber();
    }

    @Override
    public String getBlockHash() {
        return getBlock().getBlockHash();
    }
}

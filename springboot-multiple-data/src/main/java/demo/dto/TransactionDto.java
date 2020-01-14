package demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    private Long id;
    private String transactionHash;
    private Integer transactionIndex;
    private String blockHash;
    private Long blockNumber;
}

package demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockDto {

    private Long id;
    private Long blockNumber;
    private String blockHash;
    private List<String> transactionHashes;
    private Integer transactionCount;
}

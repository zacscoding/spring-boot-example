package demo.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.domain.Block;
import demo.domain.Transaction;
import demo.domain.jpa.JpaBlock;
import demo.domain.jpa.JpaDomainAssembler;
import demo.dto.BlockDto;
import demo.repository.BlockRepository;
import demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ChainController<B extends Block, T extends Transaction> {

    private final BlockRepository<B> blockRepository;
    private final TransactionRepository<T> transactionRepository;

    @GetMapping("/blocks")
    public List<BlockDto> getBlocks() {
        Iterable<B> iterable = blockRepository.findAll();
        List<BlockDto> result = new ArrayList<>();

        for (B block : iterable) {
            if (block instanceof JpaBlock) {
                result.add(JpaDomainAssembler.convertBlockDto((JpaBlock) block));
            } else {
                throw new UnsupportedOperationException("Not supported block type " + block.getClass());
            }
        }

        return result;
    }

}

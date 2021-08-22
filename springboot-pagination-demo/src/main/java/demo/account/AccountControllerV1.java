package demo.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/api/accounts")
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AccountControllerV1 {

    private final AccountRepository accountRepository;

    @GetMapping()
    public Page<AccountEntity> page(Pageable pageable) {
        logger.info("/search called. pageable: {}", pageable);
        return accountRepository.findAll(pageable);
    }
}

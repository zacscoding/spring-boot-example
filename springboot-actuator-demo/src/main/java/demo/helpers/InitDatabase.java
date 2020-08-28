package demo.helpers;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import demo.account.AccountEntity;
import demo.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDatabase {

    private final AccountRepository accountRepository;

    @PostConstruct
    private void setUp() {
        logger.info("## Initialize account.");
        accountRepository.saveAll(IntStream.rangeClosed(1, 15)
                                           .mapToObj(i -> {
                                               final String email = String.format("user-%d@google.com", i);
                                               final String password = UUID.randomUUID().toString();
                                               return AccountEntity.createAccount(email, password);
                                           })
                                           .collect(Collectors.toList()));
    }
}

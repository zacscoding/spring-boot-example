package demo.account;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Returns {@link AccountResource} list all
     */
    public List<AccountResource> getAccounts() {
        return accountRepository.findAll()
                                .stream()
                                .map(account -> AccountResource.builder()
                                                               .id(account.getId())
                                                               .email(account.getEmail())
                                                               .password(account.getPassword())
                                                               .build())
                                .collect(Collectors.toList());
    }
}

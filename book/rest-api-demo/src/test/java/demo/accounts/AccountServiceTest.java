package demo.accounts;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    public void findByUsername() {
        // Given
        String username = "zaccoding725@gmail.com";
        String password = "zaccoding";
        Set<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.ADMIN);
        roles.add(AccountRole.USER);
        Account account = Account.builder()
            .email(username)
            .password(password)
            .roles(roles)
            .build();
        assertThat(accountService.saveAccount(account)).isNotNull();

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(account.getEmail());

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(account.getEmail());
        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Test
    public void findByUsernameFail1() {
        String username = "zaccoding725@gmail.com";
        // Expected
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        accountService.loadUserByUsername(username);
    }

    /*
    @Test(expected = UsernameNotFoundException.class)
    public void findByUsernameEmail2() {
        accountService.loadUserByUsername("zaccoding725@gmail.com");
    }

    @Test
    public void findByUsernameFail3() {
        String username = "zaccoding725@gmail.com";
        try {
            accountService.loadUserByUsername(username);
            fail();
        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence(username);
        }
    }
    */
}
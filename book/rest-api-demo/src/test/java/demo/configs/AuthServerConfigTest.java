package demo.configs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import demo.accounts.Account;
import demo.accounts.AccountRepository;
import demo.accounts.AccountRole;
import demo.accounts.AccountService;
import demo.common.BaseControllerTest;
import demo.common.TestDescription;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // When Then
        this.mockMvc.perform(post("/oauth/token")
            .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
            .param("username", appProperties.getUserUsername())
            .param("password", appProperties.getUserPassword())
            .param("grant_type", "password"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("access_token").exists());

    }
}
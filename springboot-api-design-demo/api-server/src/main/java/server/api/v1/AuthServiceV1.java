package server.api.v1;

import org.springframework.stereotype.Service;

/**
 * Auth service
 *
 * @author zacconding
 * @Date 2018-11-30
 * @GitHub : https://github.com/zacscoding
 */
@Service
public class AuthServiceV1 {

    private String[] dummyTokens = {"aa", "bb", "cc"};

    public boolean hasAuth(String token) {
        for (String dummyToken : dummyTokens) {
            if (dummyToken.equals(token)) {
                return true;
            }
        }

        return false;
    }
}

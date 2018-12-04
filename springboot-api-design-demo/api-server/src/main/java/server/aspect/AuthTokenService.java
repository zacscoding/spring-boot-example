package server.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import server.api.ApiStatusCode;
import server.api.ResponseDTO;
import server.util.ServletUtil;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@ConditionalOnProperty(name = "auth.token.enable", havingValue = "true")
@Component
@Aspect
public class AuthTokenService {

    private String[] authTokens = new String[] {"aa", "bb", "cc"};
    private String[] forbiddenTokens = new String[] {"dd", "ee"};

    @Around("@annotation(server.aspect.annotation.AuthTokenPreFilter)")
    public Object filterAuthToken(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Pre filter : {}", pjp.getSignature());
        String token = ServletUtil.getHeader("AUTH_TOKEN");

        for (String authToken : authTokens) {
            if (authToken.equals(token)) {
                return pjp.proceed();
            }
        }

        for (String forbiddenToken : forbiddenTokens) {
            if (forbiddenToken.equals(token)) {
                return ResponseDTO.createException(ApiStatusCode.FORBIDDEN);
            }
        }

        return ResponseDTO.createException(ApiStatusCode.UNAUTHORIZED);
    }
}

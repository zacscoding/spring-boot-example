package server.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import server.api.ApiStatus;
import server.api.ResponseDTO;
import server.util.ServletUtil;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
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
                return ResponseDTO.createException(ApiStatus.FORBIDDEN);
            }
        }

        return ResponseDTO.createException(ApiStatus.UNAUTHORIZED);
    }
}

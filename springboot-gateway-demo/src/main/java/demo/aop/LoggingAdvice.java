package demo.aop;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * method logging advice
 */
@Slf4j
@Component
@Aspect
public class LoggingAdvice {

    @Around("@annotation(demo.aop.annotation.Loggings)")
    public Object logProcess(ProceedingJoinPoint pjp) throws Throwable {
        String id = pjp.getTarget().getClass().getSimpleName() + " :: " + pjp.getSignature().getName();
        String param = null;

        try {
            param = Arrays.toString(pjp.getArgs());
        } catch (Exception e) {
            param = "Failed to parse params : " + e.getMessage();
        }

        logger.info("## ======================Before Catch =================================== ##");
        logger.info("ID : {}", id);
        logger.info("Param : {}", param);
        logger.info("## ===================================================================== ##");
        return pjp.proceed();
    }
}

package demo.aop;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@Aspect
public class MethodInvokePrintAdvice {

    @Before("@annotation(demo.aop.annotation.PrintInvoke)")
    public void processPreValidation(JoinPoint joinPoint) {
        logger.info("{} is called. args : {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }
}

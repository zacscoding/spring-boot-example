package org.boot.configuration.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2018-04-26
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@Aspect
public class FilterAdvice {

    @Around("@annotation(org.boot.configuration.aop.annotation.PostFilter)")
    public Object filter(ProceedingJoinPoint pjp) throws Throwable{
        Object result = pjp.proceed();
        try {
            String id= getIdFromSignature(pjp.getSignature());
            System.out.println("## Filter " + id);
        } catch(Exception e) {
            log.error("Failed to filter", e);
        } finally {
            return result;
        }
    }

    private String getIdFromSignature(Signature signature) {
        String id = null;
        if (signature == null || !StringUtils.hasText((id = signature.toString()))) {
            return null;
        }

        int blankIdx = id.indexOf(' ');
        return id.substring(blankIdx + 1, id.length());
    }
}

package org.bootdemo.aop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bootdemo.entity.Pair;
import org.bootdemo.util.GsonUtil;
import org.bootdemo.validator.PageRequestRule;
import org.bootdemo.validator.ParameterRule;
import org.bootdemo.validator.ParameterRule.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Test validation rules1
 *
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@Aspect
public class ValidationCheckAdvice {

    private ReentrantLock lock = new ReentrantLock();
    private Map<String, FilterEntity> cache = new HashMap<>();

    @Around("execution(* org.bootdemo.web.*Controller.*(..))")
    public Object processPreValidation(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String id = getIdFromSignature(pjp.getSignature());
        System.out.println("## Check : " + id + "\n" + GsonUtil.toStringPretty(pjp.getArgs()));
        FilterEntity filter = cache.get(id);
        if (filter == null) {
            try {
                lock.lock();
                if ((filter = cache.get(id)) == null) {
                    filter = new FilterEntity();
                    try {
                        filter.uri = getHttpServletRequest().getRequestURI();
                        filter.paramRules = paramRules(filter.uri);
                        filter.isFiltered = filter.paramRules != null;
                    } catch (Exception e) {
                        filter.isFiltered = false;
                        // ignore
                    }
                    cache.put(id, filter);
                }
            } finally {
                lock.unlock();
            }
        }

        if (!filter.isFiltered) {
            return pjp.proceed();
        }

        ValidationResult result = null;

        for (Pair<Integer, List<ParameterRule>> rules : filter.paramRules) {
            Object arg = pjp.getArgs()[rules.getLeft()];
            for (ParameterRule rule : rules.getRight()) {
                result = rule.validate(arg);
                if (!result.success) {
                    long elapsed = System.currentTimeMillis() - start;
                    System.out.println("## filter elapsed : " + (elapsed));
                    throw new RuntimeException(result.error);
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("## filter elapsed : " + (elapsed));
        return pjp.proceed();
    }

    private String getIdFromSignature(Signature signature) {
        String id = signature.toString();

        // 리턴값 제외 추출(클래스,메소드이름,매개변수 타입으로 구분 가능하므로)
        int blankIdx = id.indexOf(' ');
        return id.substring(blankIdx + 1, id.length());
    }


    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();

        if (ra == null) {
            return null;
        }

        return ((ServletRequestAttributes) ra).getRequest();
    }

    private static class FilterEntity {
        private String uri;
        private boolean isFiltered;
        private List<Pair<Integer, List<ParameterRule>>> paramRules;
    }

    private List<Pair<Integer, List<ParameterRule>>> paramRules(String uri) {
        System.out.println("Check uri : " + uri);
        if ("/map".equals(uri)) {
            return Arrays.asList(new Pair<>(0, Arrays.asList(new PageRequestRule())));
        } else {
            return null;
        }
    }
}
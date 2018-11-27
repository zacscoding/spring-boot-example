package org.bootdemo.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.bootdemo.annotation.PreFilter;
import org.bootdemo.validator.ParameterRule;
import org.bootdemo.validator.ParameterRule.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
@Component
@Aspect
public class ValidationCheckAdvice2 {

    private static final Logger logger = LoggerFactory.getLogger(ValidationCheckAdvice2.class);
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, ParameterRule[][]> filterCache = new HashMap<>();

    @Autowired
    private ApplicationContext ctx;

    @Before("@annotation(org.bootdemo.annotation.PreFilter)")
    public void preFilter(JoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        String id = getIdFromSignature(joinPoint.getSignature());
        ParameterRule[][] rules = filterCache.get(id);
        if (rules == null) {
            try {
                lock.lock();
                if ((rules = filterCache.get(id)) == null) {
                    rules = extractFilterRules(joinPoint);
                }
                filterCache.put(id, rules);
            } finally {
                lock.unlock();
            }
        }

        for (int i = 0; i < rules.length; i++) {
            if (rules[i] == null || rules[i].length == 0) {
                continue;
            }

            for (ParameterRule rule : rules[i]) {
                ValidationResult result = rule.validate(joinPoint.getArgs()[i]);
                if (result != null && !result.success) {
                    logger.trace("Prefilter fail : " + result.error);
                    throw new RuntimeException(result.error);
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        logger.info("## Prefilter elapsed : " + elapsed);
    }

    private String getIdFromSignature(Signature signature) {
        String id = signature.toString();

        // 리턴값 제외 추출(클래스,메소드이름,매개변수 타입으로 구분 가능하므로)
        int blankIdx = id.indexOf(' ');
        return id.substring(blankIdx + 1, id.length());
    }

    private ParameterRule[][] extractFilterRules(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PreFilter prefilter = method.getAnnotation(PreFilter.class);

        String[] ruleBeans = prefilter.argRules();

        if (joinPoint.getArgs().length != method.getParameterCount()) {
            throw new RuntimeException(String
                .format("Invalid @Prefilter rules value. Please match rules and args length. arg : %d, method param type : %d", joinPoint.getArgs().length,
                    method.getParameterCount()));
        }

        ParameterRule[][] rules = new ParameterRule[ruleBeans.length][];

        for (int i = 0; i < rules.length; i++) {
            if (ruleBeans[i] == null) {
                continue;
            }

            StringTokenizer st = new StringTokenizer(ruleBeans[i], ",");
            int numberOfBeans = st.countTokens();
            rules[i] = new ParameterRule[numberOfBeans];
            for (int j = 0; j < numberOfBeans; j++) {
                String beanName = st.nextToken();
                Object bean = ctx.getBean(beanName);
                if (bean == null || !(bean instanceof ParameterRule)) {
                    throw new RuntimeException(String.format("Bean name : %s is null or can`t cast ParameterRule", beanName));
                }

                rules[i][j] = (ParameterRule) bean;
            }
        }

        return rules;
    }
}

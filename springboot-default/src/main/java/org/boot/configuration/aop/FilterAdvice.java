package org.boot.configuration.aop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.boot.configuration.aop.annotation.PostFilter;
import org.boot.configuration.aop.enums.EntityType;
import org.boot.entity.Book;
import org.boot.entity.Person;
import org.boot.service.FilteringService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FilteringService filteringService;

    private Map<String, PostFilter> persistents = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Around("@annotation(org.boot.configuration.aop.annotation.PostFilter)")
    public Object filter(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();

        if (result == null) {
            return null;
        }

        try {
            String id = getIdFromSignature(pjp.getSignature());
            // extract @PostFilter annotation
            PostFilter postFilter = persistents.get(id);
            if(postFilter == null) {
                lock.lock();
                if ((postFilter = persistents.get(id)) == null) {
                    postFilter = extractAnnotation(pjp);
                    persistents.put(id, postFilter);
                }
                lock.unlock();
                handleObject(result, postFilter);
            }
        } catch (Exception e) {
            log.error("Failed to filter", e);
        } finally {
            return result;
        }
    }

    private void handleObject(Object result, PostFilter postFilter) {
        if(result instanceof Collection<?>) {
            handleCollection((Collection<?>) result, postFilter);
        } else if(result instanceof Map<?,?>) {
            handleMap((Map<?,?>) result, postFilter);
        } else {
            if(result instanceof Person) {
                filteringService.filterPerson((Person) result);
            } else if(result instanceof Book) {
                filteringService.filterBook((Book) result);
            } else {
                log.error("Cant`t handle result : " + result.getClass().getName());
            }
        }
    }

    private void handleCollection(Collection<?> collection, PostFilter postFilter) {
        if (postFilter.entityType() == EntityType.PERSON) {
            filteringService.filterPersons((Collection<Person>) collection);
        } else if(postFilter.entityType() == EntityType.BOOk) {
            filteringService.filterBooks((Collection<Book>) collection);
        }
    }

    private void handleMap(Map<?, ?> map, PostFilter postFilter) {
        String key = postFilter.mapsKey();

        if(!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Map result must be add maps key to extract data");
        }

        log.info("is Map");
        Object data = map.get(key);
        if(data != null) {
            handleObject(data, postFilter);
        }
    }

    private PostFilter extractAnnotation(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        return methodSignature.getMethod().getAnnotation(PostFilter.class);
    }

    private String getIdFromSignature(Signature signature) {
        String id = null;
        if (signature == null || !StringUtils.hasText((id = signature.toString()))) {
            return null;
        }
        System.out.println(id);

        int blankIdx = id.indexOf(' ');
        return id.substring(blankIdx + 1, id.length());
    }
}

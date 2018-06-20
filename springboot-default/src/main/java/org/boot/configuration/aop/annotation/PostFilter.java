package org.boot.configuration.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.boot.configuration.aop.enums.EntityType;

/**
 * @author zacconding
 * @Date 2018-04-26
 * @GitHub : https://github.com/zacscoding
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostFilter {
    EntityType entityType();
    String mapsKey() default "";
}
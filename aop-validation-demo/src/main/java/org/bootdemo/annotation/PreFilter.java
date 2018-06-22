package org.bootdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreFilter {

    /**
     * index : arg`s index
     * value : bean names with comma delimiter e.g) "tempRule,pageRequestRule"
     */
    String[] argRules();
}
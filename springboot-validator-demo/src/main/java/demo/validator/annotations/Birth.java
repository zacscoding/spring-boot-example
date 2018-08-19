package demo.validator.annotations;

import demo.validator.BirthValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author zacconding
 * @Date 2018-08-19
 * @GitHub : https://github.com/zacscoding
 */
@Documented
@Constraint(validatedBy = BirthValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Birth {

    String message() default "Invalid birth format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
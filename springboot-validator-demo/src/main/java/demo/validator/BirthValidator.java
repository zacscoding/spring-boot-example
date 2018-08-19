package demo.validator;

import demo.validator.annotations.Birth;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zacconding
 * @Date 2018-08-19
 * @GitHub : https://github.com/zacscoding
 */
public class BirthValidator implements ConstraintValidator<Birth, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value != null) && value.matches("[0-9]+") && value.length() == 6;
    }
}
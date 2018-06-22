package org.bootdemo.validator;

/**
 * ref : ethj
 *
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
public abstract class ParameterRule {

    public static final ValidationResult Success = new ValidationResult(true, null);

    public abstract ValidationResult validate(Object param);

    public static class ValidationResult {

        public boolean success;
        public String error;

        public ValidationResult(boolean success, String error) {
            this.success = success;
            this.error = error;
        }

        @Override
        public String toString() {
            return "ValidationResult{" + "success=" + success + ", error='" + error + '\'' + '}';
        }
    }
}
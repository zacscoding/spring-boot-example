package demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * http://jojoldu.tistory.com/129
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationCustomException extends RuntimeException {

    private Error[] errors;

    public ValidationCustomException(String defaultMessage, String field) {
        this.errors = new Error[] {new Error(defaultMessage, field)};
    }

    public ValidationCustomException(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public static class Error {

        private String defaultMessage;
        private String field;

        public Error(String defaultMessage, String field) {
            this.defaultMessage = defaultMessage;
            this.field = field;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public String getField() {
            return field;
        }
    }
}

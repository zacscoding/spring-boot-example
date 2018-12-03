package server.api.exception;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
public class DuplicateValueException extends RuntimeException {

    public DuplicateValueException() {
        super();
    }

    public DuplicateValueException(String message) {
        super(message);
    }
}

package demo.exception;

/**
 * Common database exception
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message, Throwable parent) {
        super(message, parent);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable parent) {
        super(parent);
    }
}

package demo.client;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
public class NoAvailableClientException extends Exception {

    public NoAvailableClientException() {
        super();
    }

    public NoAvailableClientException(String message) {
        super(message);
    }
}

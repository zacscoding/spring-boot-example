package server.api.v1;

/**
 * @author zacconding
 * @Date 2018-11-30
 * @GitHub : https://github.com/zacscoding
 */
public class ApiV1Exception extends RuntimeException {

    private ApiV1ResponseCode status;
    private String message;

    public ApiV1Exception(ApiV1ResponseCode status) {
        this.status = status;
        this.message = status.getMessage();
    }

    public ApiV1Exception(ApiV1ResponseCode status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiV1Exception(ApiV1ResponseCode status, Exception e) {
        super(e);
        this.status = status;
        this.message = status.getMessage();
    }

    public ApiV1Exception(ApiV1ResponseCode status, String message, Exception e) {
        super(e);
        this.status = status;
        this.message = message;
    }
}

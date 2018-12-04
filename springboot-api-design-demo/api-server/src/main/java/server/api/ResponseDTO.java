package server.api;

import lombok.Getter;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Getter
public class ResponseDTO<T> {

    private ApiStatus status;
    private T data;

    public static <T> ResponseDTO<T> createOK(T data) {
        return new ResponseDTO<>(new ApiStatus(ApiStatusCode.OK.getErrorCode(), ApiStatusCode.OK.getMessage()), data);
    }

    public static <T> ResponseDTO<T> createException(ApiStatusCode status) {
        return createException(status, status.getMessage());
    }

    public static <T> ResponseDTO<T> createException(ApiStatusCode code, String message) {
        return createException(code.getErrorCode(), message);
    }

    public static <T> ResponseDTO<T> createException(int errorCode, String message) {
        return new ResponseDTO<>(new ApiStatus(errorCode, message), null);
    }

    private ResponseDTO(ApiStatus status, T data) {
        this.status = status;
        this.data = data;
    }
}

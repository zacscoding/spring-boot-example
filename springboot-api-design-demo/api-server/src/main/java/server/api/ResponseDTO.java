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
        return new ResponseDTO<>(ApiStatus.OK, data);
    }

    public static <T> ResponseDTO<T> createException(ApiStatus status) {
        return new ResponseDTO<>(status, null);
    }

    public static <T> ResponseDTO<T> createException(int errorCode, String message) {
        return new ResponseDTO<>(new ApiStatus(errorCode, message), null);
    }

    public static <T> ResponseDTO<T> createException(ApiStatus status, String message) {
        return new ResponseDTO<>(new ApiStatus(status.getErrorCode(), message), null);
    }

    private ResponseDTO(ApiStatus status, T data) {
        this.status = status;
        this.data = data;
    }
}

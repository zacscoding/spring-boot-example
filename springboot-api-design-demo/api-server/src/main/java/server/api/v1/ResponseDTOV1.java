package server.api.v1;

import lombok.Getter;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Getter
public class ResponseDTOV1<T> {

    private ApiStatusV1 status;
    private T data;

    public static <T> ResponseDTOV1<T> createOK(T data) {
        return new ResponseDTOV1<>(new ApiStatusV1(ApiStatusCodeV1.OK.getErrorCode(), ApiStatusCodeV1.OK.getMessage()), data);
    }

    public static <T> ResponseDTOV1<T> createException(ApiStatusCodeV1 status) {
        return createException(status, status.getMessage());
    }

    public static <T> ResponseDTOV1<T> createException(ApiStatusCodeV1 code, String message) {
        return createException(code.getErrorCode(), message);
    }

    public static <T> ResponseDTOV1<T> createException(int errorCode, String message) {
        return new ResponseDTOV1<>(new ApiStatusV1(errorCode, message), null);
    }

    private ResponseDTOV1(ApiStatusV1 status, T data) {
        this.status = status;
        this.data = data;
    }
}

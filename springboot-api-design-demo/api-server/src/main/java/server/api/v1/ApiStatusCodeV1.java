package server.api.v1;

import lombok.Getter;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Getter
public enum ApiStatusCodeV1 {

    OK(0, "")
    , BAD_REQUEST(400, "Bad Request")
    , UNAUTHORIZED(401, "Unauthorized")
    , FORBIDDEN(403, "Forbidden")
    , INTERNAL_SERVER_ERROR(500, "Internal Server error");

    private int errorCode;
    private String message;

    ApiStatusCodeV1(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

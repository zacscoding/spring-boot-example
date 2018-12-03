package server.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@AllArgsConstructor
public class ApiStatus {

    public static final ApiStatus OK = new ApiStatus(0, "");
    public static final ApiStatus BAD_REQUEST = new ApiStatus(400, "Bad Request");
    public static final ApiStatus UNAUTHORIZED = new ApiStatus(401, "Unauthorized");
    public static final ApiStatus FORBIDDEN = new ApiStatus(403, "Forbidden");
    public static final ApiStatus INTERNAL_SERVER_ERROR = new ApiStatus(500, "Internal Server error");

    private int errorCode;
    private String message;
}

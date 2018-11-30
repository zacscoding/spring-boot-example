package server.api.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zacconding
 * @Date 2018-11-30
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@AllArgsConstructor
public enum ApiV1ResponseCode {

    OK("SUCCESS")
    , BAD_REQUEST("BAD REQUEST")
    , UNAUTHORIZED("UN AUTHORIZED");

    private String message;
}

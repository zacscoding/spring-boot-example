package client.api.v1;

import client.api.v1.ApiStatusV1;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zacconding
 * @Date 2018-12-04
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTOV1<T> {

    private ApiStatusV1 status;
    private T data;
}

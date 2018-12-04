package client.api;

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
public class ResponseDTO<T> {

    private ApiStatus status;
    private T data;
}

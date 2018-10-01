package demo.rpc2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zacconding
 * @Date 2018-10-01
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
public class Rpc2Response {

    private String requestId;
    private String rawResponse;
}

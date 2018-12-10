package client.api.v1.person;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zacconding
 * @Date 2018-12-04
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonV1 {

    private int age;
    private String id;
    private String name;
    private List<String> hobbies;
}

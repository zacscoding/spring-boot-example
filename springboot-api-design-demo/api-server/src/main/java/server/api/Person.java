package server.api;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zacconding
 * @Date 2018-11-30
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@AllArgsConstructor
public class Person {

    private int age;
    private String name;
    private List<String> hobbies;
}

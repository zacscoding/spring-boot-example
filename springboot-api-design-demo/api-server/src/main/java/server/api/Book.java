package server.api;

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
public class Book {

    private int number;
    private String name;
    private int price;
}

package org.swaggerdemo.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
public class Person {
    private Integer id;
    private String name;
    private Integer age;
    private String hobby;

    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Person person = (Person) o;
        return (Objects.equals(id, person.id));
    }
}

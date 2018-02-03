package org.swaggerdemo.demo.domain;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(notes = "Person`s id (unique)")
    private Integer id;
    @ApiModelProperty(notes = "Person`s name")
    private String name;
    @ApiModelProperty(notes = "Person`s age")
    private Integer age;
    @ApiModelProperty(notes = "Person`s hobby")
    private String hobby;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;
        return (Objects.equals(id, person.id));
    }
}

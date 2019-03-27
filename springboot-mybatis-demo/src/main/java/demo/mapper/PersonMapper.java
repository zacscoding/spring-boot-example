package demo.mapper;

import demo.model.Person;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Mapper
public interface PersonMapper {

    @Select("SELECT * FROM PERSON WHERE ID = #{id}")
    Person findById(@Param("id") Integer id);

    @Insert("INSERT INTO PERSON VALUES (#{p.id}, #{p.name})")
    int save(@Param("p") Person person);

    List<Person> findAll();
}

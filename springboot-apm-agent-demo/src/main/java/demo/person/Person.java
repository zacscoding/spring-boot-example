package demo.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zacconding
 * @Date 2019-01-15
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ElementCollection
    private List<String> hobbies;

    public static List<String> randomHobbies() {
        String[] hobbies = {
            "coding", "movie", "football", "book", "gaming"
        };

        List<String> ret = new ArrayList<>();
        Random random = new Random();

        for (String hobby : hobbies) {
            if (random.nextInt(10) % 2 == 0) {
                ret.add(hobby);
            }
        }

        if (ret.isEmpty()) {
            ret.add(hobbies[random.nextInt(hobbies.length)]);
        }

        return ret;
    }
}

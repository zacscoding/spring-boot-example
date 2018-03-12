package readinglist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zaccoding github : https://github.com/zacscoding
 */
@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    private Reader reader;
    private String isbn;
    private String title;
    private String author;
    private String description;
}

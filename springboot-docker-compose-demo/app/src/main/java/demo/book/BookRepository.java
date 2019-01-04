package demo.book;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zacconding
 * @Date 2019-01-04
 * @GitHub : https://github.com/zacscoding
 */
public interface BookRepository extends JpaRepository<Book, Long> {

}

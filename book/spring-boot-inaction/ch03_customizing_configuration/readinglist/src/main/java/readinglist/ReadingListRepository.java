package readinglist;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zaccoding github : https://github.com/zacscoding
 */
public interface ReadingListRepository extends JpaRepository<Book, Long> {

    List<Book> findByReader(Reader reader);
}

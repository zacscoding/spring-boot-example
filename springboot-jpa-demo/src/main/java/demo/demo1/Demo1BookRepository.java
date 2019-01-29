package demo.demo1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface Demo1BookRepository extends JpaRepository<Demo1Book, Long> {

    // select demo1book0_.book_id as book_id1_0_, demo1book0_.author as author2_0_, demo1book0_.price as price3_0_, demo1book0_.title as title4_0_ from demo1_book demo1book0_ left outer join demo1_book_category categories1_ on demo1book0_.book_id=categories1_.book_id left outer join demo1_category demo1categ2_ on categories1_.category_id=demo1categ2_.category_id where demo1categ2_.name=?
    List<Demo1Book> findByCategories_name(String categoryName);

    @Query(value =
        "SELECT b.*, c.* FROM DEMO1_BOOK as b LEFT OUTER JOIN DEMO1_BOOK_CATEGORY as bc ON b.BOOK_ID=bc.BOOK_ID "
            + "LEFT OUTER JOIN DEMO1_CATEGORY as c ON bc.CATEGORY_ID=c.CATEGORY_ID WHERE c.name in :categoryNames"
            + "GROUP BY b.BOOK_ID"
        , nativeQuery = true)
    List<Demo1Book> findByCategories_nameIn(List<String> categoryNames);
}

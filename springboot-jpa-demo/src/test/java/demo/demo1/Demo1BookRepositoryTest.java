package demo.demo1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class Demo1BookRepositoryTest {

    @Autowired
    private Demo1BookRepository bookRepository;
    @Autowired
    private Demo1CategoryRepository categoryRepository;

    @Test
    public void test_findByCategories_something() {
        // given
        Demo1Category computerCategory = categoryRepository.save(Demo1Category.builder().name("computer").build());
        Demo1Category mathCategory = categoryRepository.save(Demo1Category.builder().name("math").build());
        Demo1Category etcCategory = categoryRepository.save(Demo1Category.builder().name("etc").build());

        Demo1Book book1 = bookRepository.save(Demo1Book.builder().title("zaccoding`s math computer")
            .author("zaccoding")
            .price(1)
            .categories(Arrays.asList(computerCategory, mathCategory))
            .build());

        Demo1Book book2 = bookRepository.save(Demo1Book.builder().title("zaccodings confused mathematics :(")
            .author("zaccoding")
            .price(1)
            .categories(Arrays.asList(mathCategory, etcCategory))
            .build());

        Demo1Book book3 = bookRepository.save(Demo1Book.builder().title("zaccodings etc book :)")
            .author("zaccoding")
            .price(1)
            .categories(Arrays.asList(etcCategory))
            .build());

        // test1 : findByCategories_name
        List<Demo1Book> search1 = bookRepository.findByCategories_name(mathCategory.getName());
        assertThat(search1.size()).isEqualTo(2);
        for (Demo1Book searchBook1 : search1) {
            assertThat(containsCategory(searchBook1, mathCategory.getName())).isTrue();
        }

        // test2 : findByCategories_nameIn
        List<Demo1Book> search2 = bookRepository.findByCategories_nameIn(
            Arrays.asList(computerCategory.getName(), mathCategory.getName())
        );

        assertThat(search2.size()).isEqualTo(2);
    }


    private boolean containsCategory(Demo1Book book, String categoryName) {
        for (Demo1Category category : book.getCategories()) {
            if (category.getName().equals(categoryName)) {
                return true;
            }
        }

        return false;
    }

}

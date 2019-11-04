package demo.demo1;

import static org.assertj.core.api.Java6Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@Ignore
public class ManyToManyTests {

    @Autowired
    private D1BookRepository bookRepository;
    @Autowired
    private D1CategoryRepository categoryRepository;

    private D1Category educationCategory;
    private D1Category computersCategory;

    private D1Book computerBook;
    private D1Book mathBook;

    @Before
    public void setUp() {
        setupCategories();
        setupBooks();
        setupRelations();
    }

    @Test
    @Transactional
    public void runTests() {
        D1Category computers = categoryRepository.findById(computersCategory.getId()).get();

        assertThat(computers.getBooks().size()).isEqualTo(1);

        D1Category educations = categoryRepository.findById(educationCategory.getId()).get();
        assertThat(educations.getBooks().size()).isEqualTo(2);

        D1Book find1 = bookRepository.findById(computerBook.getId()).get();
        assertThat(find1.getCategories().size()).isEqualTo(2);

        D1Book find2 = bookRepository.findById(mathBook.getId()).get();
        assertThat(find2.getCategories().size()).isEqualTo(1);
    }

    private void setupBooks() {
        computerBook = bookRepository.save(
                D1Book.builder()
                      .author("zaccoding")
                      .price(100)
                      .title("Zaccoding's easy java")
                      .build());

        mathBook = bookRepository.save(
                D1Book.builder()
                      .author("zaccoding")
                      .price(50)
                      .title("Zaccoding's easy mathematics")
                      .build());
    }

    private void setupCategories() {
        educationCategory = categoryRepository.save(D1Category.builder()
                                                              .name("Education")
                                                              .build());

        computersCategory = categoryRepository.save(D1Category.builder()
                                                              .name("Computers&Tech")
                                                              .build());
    }

    private void setupRelations() {
        educationCategory.addBook(mathBook);
        educationCategory.addBook(computerBook);

        computersCategory.addBook(computerBook);
    }
}

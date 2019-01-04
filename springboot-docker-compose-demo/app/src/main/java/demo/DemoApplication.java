package demo;

import demo.book.Book;
import demo.book.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    BookRepository bookRepository;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            BookRepository bookRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Random random = new Random();
                List<Book> books = new ArrayList<>(32);

                for (int i = 1; i <= 32; i++) {
                    books.add(
                        Book.builder()
                            .author("Author" + i)
                            .name("Book" + i)
                            .price(random.nextInt(40000) + 10000)
                            .build()
                    );
                }

                bookRepository.saveAll(books);
            }
        };
    }

    @GetMapping("/")
    public List<Book> index() {
        return bookRepository.findAll();
    }
}

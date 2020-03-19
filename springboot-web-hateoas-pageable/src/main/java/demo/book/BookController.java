package demo.book;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @PostConstruct
    private void setUp() {
        initDummyData();
    }

    @GetMapping("/books")
    public List<BookEntity> getBooks(Pageable pageable) {
        logger.info("# Request to get books. {}", pageable);

        Page<BookEntity> pages = bookRepository.findAll(pageable);

        logger.info("# Check pages");
        logger.info(">> page number : {}", pages.getNumber());
        logger.info(">> total pages : {}", pages.getTotalPages());

        return pages.getContent();
    }

    @GetMapping("/books2")
    public ResponseEntity getBooks2(Pageable pageable,
                                    PagedResourcesAssembler<BookResource> assembler) {
        logger.info("# Request to get books2. {}", pageable);

        Page<BookEntity> pages = bookRepository.findAll(pageable);

        logger.info("# Check pages");
        logger.info(">> page number : {}", pages.getNumber());
        logger.info(">> total pages : {}", pages.getTotalPages());

        return ResponseEntity.ok();
    }

    @GetMapping("/book/{bookId}")
    public BookResource getBook(@PathVariable("bookId") Long bookId) {
        Optional<BookEntity> bookOptional = bookRepository.findById(bookId);

        return bookOptional.map(bookEntity -> new BookResource(bookEntity)).orElse(null);
    }

    private void initDummyData() {
        int size = 100;
        Random rand = new Random();

        for (int i = 1; i <= size; i++) {
            final BookEntity book = BookEntity.builder()
                                              .title("Book-" + i)
                                              .price((rand.nextInt(20) + 1) * 100)
                                              .build();

            bookRepository.save(book);
        }
    }
}

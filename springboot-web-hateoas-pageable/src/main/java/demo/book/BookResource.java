package demo.book;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.hateoas.Link;

import lombok.Getter;

@Getter
public class BookResource {

    private long id;

    private String title;

    private int price;

    private List<Link> links;

    public BookResource(BookEntity entity, Link... links) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();

        this.links = links == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(links));
        this.links.add(
                linkTo(BookController.class)
                        .slash("book")
                        .slash(entity.getId())
                        .withSelfRel()
        );
    }
}

package demo.demo1;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "books" })
@Entity(name = "DEMO1_CATEGORY")
public class D1Category {

    @Id
    @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;
    private String name;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<D1Book> books = new ArrayList<>();

    public boolean addBook(D1Book book) {
        if (books.contains(requireNonNull(book, "book"))) {
            throw new IllegalStateException("Already exist book in category " + name);
        }

        book.addCategory(this);

        return books.add(book);
    }
}

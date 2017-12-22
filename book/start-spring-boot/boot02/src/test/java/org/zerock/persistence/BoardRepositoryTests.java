package org.zerock.persistence;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Board;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author zacconding
 * @Date 2017-12-16
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepo;
    private Board board1;
    @Before
    public void setUp() {
        boardRepo.deleteAll();
        board1 = new Board();
        board1.setTitle("title");
        board1.setContent("content");
        board1.setWriter("user00");
    }

    @Test
    @Ignore
    public void inspect() {
        Class<?> clazz = boardRepo.getClass();
        System.out.println("## boardRepo class name :: " + clazz.getName());

        Class<?>[] interfaces = clazz.getInterfaces();
        Stream.of(interfaces).forEach(inter -> System.out.println("## interface : " + inter.getName()));
    }

    @Test
    @Ignore
    public void saveAndFindOne() {
        System.out.println("## start save");

        Board saved = boardRepo.save(board1);

        System.out.println("## start find");
        Board find = boardRepo.findOne(saved.getBno());
        System.out.println("## find : " + find);
    }

    @Test
    @Ignore
    public void update() {
        System.out.println("## save");
        Board saved = boardRepo.save(board1);
        long id = saved.getBno();

        System.out.println("## find id : " + id);
        Board find = boardRepo.findOne(id);

        System.out.println("## update title");
        find.setTitle("modified title");

        // select -> update
        System.out.println("## save again with diff title");
        boardRepo.save(find);

        // select
        System.out.println("## save again with same title");
        boardRepo.save(find);
    }

    @Test
    public void delete() {
        System.out.println("## save");
        Board saved = boardRepo.save(board1);

        System.out.println("## delete by id : " + saved.getBno());
        boardRepo.delete(saved.getBno());
    }


}

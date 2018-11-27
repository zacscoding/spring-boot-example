package org.zerock.persistence;

import lombok.extern.java.Log;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.WebBoard;

import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

/**
 * @author zacconding
 * @Date : 2017-12-20
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class WebBoardRepositoryTest {

    @Autowired
    WebBoardRepository repo;

    @Test
    //@Ignore
    public void insertBoardDummies() {
        IntStream.range(0, 300).forEach((int i) -> {
            WebBoard board = new WebBoard();

            board.setTitle("Sample Board Title : " + i);
            board.setContent("Content Sample ... " + i + " of Board ");
            board.setWriter("user0" + (i % 10));

            repo.save(board);
        });
    }

    @Test
    @Ignore
    public void testList1() {
        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "bno");

        Page<WebBoard> result = repo.findAll(repo.makePredicate(null, null), pageable);

        log.info("## PAGE : " + result.getPageable());

        result.getContent().forEach(board -> {
            log.info("" + board);
        });
    }

    @Test
    @Ignore
    public void testList2() {
        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "bno");
        Page<WebBoard> result = repo.findAll(repo.makePredicate("t", "10"), pageable);

        log.info("## PAGE : " + result.getPageable());

        log.info("## display search list");
        result.getContent().forEach(board -> {
            log.info("" + board);
            assertTrue(board.getTitle().contains("10"));
        });


    }


}

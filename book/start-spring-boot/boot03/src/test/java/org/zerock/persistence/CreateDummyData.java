package org.zerock.persistence;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.AbstractTestRunner;
import org.zerock.domain.Board;
import org.zerock.persistence.BoardRepository;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public class CreateDummyData extends AbstractTestRunner {

    @Autowired
    BoardRepository boardRepo;

    @Before
    public void setUp() {
        boardRepo.deleteAll();
    }

    @Test
    @Ignore
    public void insertDummy() {
        for (int i = 1; i <= 200; i++) {
            Board board = new Board();
            board.setTitle("제목.." + i);
            board.setContent("내용 ...." + i + " 채우기 ");
            board.setWriter("user0" + (i % 10));
            boardRepo.save(board);
        }
    }
}

package org.zerock.persistence;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.AbstractTestRunner;
import org.zerock.domain.Board;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public class BoardRepostitoryTest extends AbstractTestRunner {
    @Autowired
    BoardRepository boardRepository;

    @Test
    public void findBoardByTitle() {
        String title = "제목..177";
        boardRepository.findBoardByTitle(title).forEach(board -> {
            assertThat(board.getTitle(),is(title));
        });
    }

    @Test
    public void findByWriter() {
        String writer = "user00";
        boardRepository.findByWriter(writer).forEach(board -> {
            assertThat(board.getWriter(), is(writer));
            System.out.println(board);
        });
    }

    @Test
    public void findByWriterContaining() {
        String keyword = "05";
        boardRepository.findByWriterContaining(keyword).forEach(board -> {
            assertTrue(board.getWriter().contains(keyword));
        });
    }

    @Test
    public void findByTitleContainingAndBnoGreaterThan() {
        String title = "5";
        Long bno = 50L;

        boardRepository.findByTitleContainingAndBnoGreaterThan(title,bno).forEach(board -> {
            assertTrue(board.getTitle().contains(title));
            assertTrue(board.getBno() > bno);
        });
    }

    @Test
    public void findByBnoGreaterThanOrderByBnoDesc() {
        Long bno = 90L;
        boardRepository.findByBnoGreaterThanOrderByBnoDesc(bno).forEach(board -> {
            assertTrue(board.getBno() > bno);
            System.out.println("# bno " + board.getBno());
        });
    }

    @Test
    public void findByBnoGreaterThanOrderByBnoDescWithPageable() {
        /*Long bno = 0L;
        Pageable pageable = new PageRequest(0,10, Sort.Direction.ASC, "bno");
        List<Board> results = boardRepository.findByBnoGreaterThan(bno,pageable);
        assertTrue(results.size() <= 10);
        results.forEach(board -> {
            System.out.println(board);
        });*/
    }

    @Test
    public void findByBnoGreateThanWithPage() {
        Long bno = 0L;
        Pageable pageable = new PageRequest(0,10,Sort.Direction.ASC, "bno");

        Page<Board> results = boardRepository.findByBnoGreaterThan(bno,pageable);

        System.out.println("## PAGE SIZE getSize() : " + results.getSize());
        System.out.println("## TOTAL PAGES getTotalPages() : " + results.getTotalPages());
        System.out.println("## TOTAL COUNT getTotalElements() : " + results.getTotalElements());
        System.out.println("## NEXT nextPageable() : " + results.nextPageable());

        List<Board> boards = results.getContent();
        boards.forEach(board -> {System.out.println(board);});
    }
}

package org.zerock.persistence;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.FreeBoard;
import org.zerock.domain.FreeBoardReply;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class FreeBoardTests {
    @Autowired
    FreeBoardRepository boardRepository;
    @Autowired
    FreeBoardReplyRepository replyRepository;

    @Test
    public void insertDummy() {
        IntStream.range(1,200).forEach(i -> {
            FreeBoard board = new FreeBoard();
            board.setTitle("Free Board ... " + i);
            board.setContent("Free Content .... " + i);
            board.setWriter("user" + i%10);

            boardRepository.save(board);
        });
    }

    @Transactional
    @Test
    public void insertReply2Way() {
        Optional<FreeBoard> result = boardRepository.findById(199L);
        result.ifPresent(board -> {
            log.info("# exist board -> save reply");
            List<FreeBoardReply> replies = board.getReplies();

            FreeBoardReply reply = new FreeBoardReply();
            reply.setReply("REPLY.........");
            reply.setReplyer("replyer00");
            reply.setBoard(board);

            replies.add(reply);

            boardRepository.save(board);
        });
    }

    @Test
    public void insertReply1Way() {
        FreeBoard board = new FreeBoard();
        board.setBno(199L);

        FreeBoardReply reply = new FreeBoardReply();
        reply.setReply("REPLY.........");
        reply.setReplyer("replyer00");
        reply.setBoard(board);

        replyRepository.save(reply);
    }

    @Test
    public void testList1() {
        // Spring boot 2.0 new PageRequest @Deprecated :: -> PageRequest.of
        Pageable page = PageRequest.of(0,10, Sort.Direction.DESC, "bno");
        boardRepository.findByBnoGreaterThan(0L,page).forEach(board -> {
            log.info(board.getBno() + " : " + board.getTitle());
        });
    }

    @Transactional
    @Test
    public void testList2() {
        Pageable page = PageRequest.of(0,10, Sort.Direction.DESC, "bno");
        boardRepository.findByBnoGreaterThan(0L,page).forEach(board -> {
            log.info(board.getBno() + " : " + board.getTitle() + " : " + board.getReplies().size());
        });
    }

    @Test
    public void testList3() {
        Pageable page = PageRequest.of(0,10, Sort.Direction.DESC, "bno");

        boardRepository.getPage(page).forEach(arr -> {
           log.info(Arrays.toString(arr));
        });
    }


}

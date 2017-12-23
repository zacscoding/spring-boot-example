package org.zerock.persistence;

import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebReply;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author zacconding
 * @Date 2017-12-23
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class ReplyRepositoryTest {
    @Autowired
    private WebBoardRepository boardRepo;
    @Autowired
    private ReplyRepository replyRepo;

    @Test
    public void testInsertReplies() {
        Long[] bnos = getTopBno(3);

        Arrays.stream(bnos).forEach(num -> {
            // create board
            WebBoard board=  new WebBoard();
            board.setBno(num);

            // create replies
            IntStream.range(0,10).forEach(i -> {
                WebReply reply = new WebReply();
                reply.setReplyText("REPLY ..." + i);
                reply.setReplyer("replyer"+i);
                reply.setBoard(board);

                replyRepo.save(reply);
            });
        });
    }

    private Long[] getTopBno(int size) {
        if(size < 1) {
            return null;
        }

        Long[] bnos = new Long[size];
        Page<WebBoard> results = boardRepo.findAll(boardRepo.makePredicate(null,null),
                PageRequest.of(0,size, Sort.Direction.DESC, "bno"));

        int idx = 0;

        for (WebBoard board : results.getContent()) {
            if(idx > bnos.length) {
                break;
            }
            bnos[idx++] = board.getBno();
        }

        return bnos;
    }




}

package org.zerock.persistence;

import com.querydsl.core.BooleanBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.AbstractTestRunner;
import org.zerock.domain.Board;
import org.zerock.domain.QBoard;

import java.util.List;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public class BoardRepositoryByQuerydslTest extends AbstractTestRunner {
    @Autowired
    BoardRepositoryByQuerydsl boardRepository;

    @Test
    public void predicate() {
        String type = "t";
        String keyword = "17";

        BooleanBuilder builder = new BooleanBuilder();
        QBoard board = QBoard.board;
        // check search type
        if(type.equals("t")) {
            builder.and(board.title.like("%" + keyword + "%"));
        }

        // bno > 0
        builder.and(board.bno.gt(0L));

        // pageable
        Pageable pageable = new PageRequest(0,10);

        Page<Board> results = boardRepository.findAll(builder, pageable);

        System.out.println("## PAGE SIZE getSize() : " + results.getSize());
        System.out.println("## TOTAL PAGES getTotalPages() : " + results.getTotalPages());
        System.out.println("## TOTAL COUNT getTotalElements() : " + results.getTotalElements());
        System.out.println("## NEXT nextPageable() : " + results.nextPageable());
        List<Board> boards = results.getContent();
        boards.forEach(b -> {System.out.println(b);});
    }



}

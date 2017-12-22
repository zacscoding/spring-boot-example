package org.zerock.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.AbstractTestRunner;

import java.util.Arrays;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public class BoardRepositoryByQueryTest extends AbstractTestRunner {
    @Autowired
    BoardRepositoryByQuery boardRepository;

    @Test
    public void findByTitle() {
        String title = "17";
        boardRepository.findByTitle(title).forEach(board -> {
            System.out.println(board);
        });
    }

    @Test
    public void findByContent() {
        String content = "155";
        boardRepository.findByContent(content).forEach(board -> {
            System.out.println(board);
        });
    }

    @Test
    public void findByWriter() {
        String writer ="01";
        boardRepository.findByWriter(writer).forEach(board -> {
            System.out.println(board);
        });
    }

    @Test
    public void findByTitle2() {
        String title = "17";
        boardRepository.findByTitle2(title).forEach(arr -> {
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void findByTitle3() {
        String title = "17";
        boardRepository.findByTitle3(title).forEach(arr -> {
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void findByPage() {
        Pageable pageble = new PageRequest(0,10);
        boardRepository.findByPage(pageble).forEach(board -> {
            System.out.println(board);
        });

    }
}

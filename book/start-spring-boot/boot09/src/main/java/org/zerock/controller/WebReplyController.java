package org.zerock.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebReply;
import org.zerock.persistence.ReplyRepository;

import java.nio.file.Path;
import java.util.List;

/**
 * @author zacconding
 * @Date 2017-12-23
 * @GitHub : https://github.com/zacscoding
 */
@RestController
@RequestMapping("/replies/*")
@Log
public class WebReplyController {
    @Autowired
    ReplyRepository replyRepo;

    @GetMapping("/{bno}")
    public ResponseEntity<List<WebReply>> getReplies(@PathVariable("bno") Long bno) {
        log.info("## [request get replies] bno : " + bno);

        WebBoard webBoard = new WebBoard();
        webBoard.setBno(bno);

        return new ResponseEntity<>(getListByBoard(webBoard),HttpStatus.OK);
    }

    @Secured(value={"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @Transactional
    @PostMapping(value = "/{bno}")
    public ResponseEntity<List<WebReply>> addReply(@PathVariable("bno") Long bno, @RequestBody WebReply reply) {
        log.info("## [request add reply] bno : " + bno + ", reply : " + reply);

        WebBoard webBoard = new WebBoard();
        webBoard.setBno(bno);

        reply.setBoard(webBoard);

        replyRepo.save(reply);
        return new ResponseEntity<>(getListByBoard(webBoard), HttpStatus.CREATED);
    }

    @Secured(value={"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @Transactional
    @PutMapping("/{bno}")
    public ResponseEntity<List<WebReply>> modifyReply(@PathVariable("bno") Long bno, @RequestBody WebReply reply) {
        log.info("## [request modify reply] bno : " + bno + ", reply : " + reply);

        replyRepo.findById(reply.getRno()).ifPresent(origin -> {
            origin.setReplyText(reply.getReplyText());
            replyRepo.save(origin);
        });

        WebBoard webBoard = new WebBoard();
        webBoard.setBno(bno);

        return new ResponseEntity<>(getListByBoard(webBoard), HttpStatus.CREATED);
    }

    @Secured(value={"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @Transactional
    @DeleteMapping("/{bno}/{rno}")
    public ResponseEntity<List<WebReply>> removeReply(@PathVariable("bno") Long bno, @PathVariable("rno") Long rno) {
        log.info("## [request delete reply] bno : " + bno + ", rno : " + rno);

        replyRepo.deleteById(rno);

        WebBoard webBoard = new WebBoard();
        webBoard.setBno(bno);

        return new ResponseEntity<>(getListByBoard(webBoard), HttpStatus.OK);
    }


    private List<WebReply> getListByBoard(WebBoard board) throws RuntimeException {
        log.info("## getListByBoard :: " + board);

        return replyRepo.getRepliesOfBoard(board);
    }




}

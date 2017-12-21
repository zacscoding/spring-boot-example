package org.zerock.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.domain.WebBoard;
import org.zerock.persistence.WebBoardRepository;
import org.zerock.vo.PageMaker;
import org.zerock.vo.PageVO;

/**
 * @author zacconding
 * @Date : 2017-12-20
 * @GitHub : https://github.com/zacscoding
 */
@Controller
@RequestMapping("/boards/")
@Log
public class WebBoardController {
    @Autowired
    private WebBoardRepository webBoardRepository;

    @GetMapping("/list")
    //@PageableDefault
    // public void list(@PageableDefault(direction = Sort.Direction.DESC, sort="bno", size=10, page=0) Pageable pageable) {
    public void list(PageVO vo, Model model) {
        Pageable page = vo.makePageable(0,"bno");

        Page<WebBoard> result = webBoardRepository.findAll(webBoardRepository.makePredicate(null,null), page);
        log.info("## [list is called] page : " + page);
        log.info("## result : " + result);
        log.info("## TOTAL PAGE NUMBER : " + result.getTotalPages());

        model.addAttribute("result", new PageMaker(result));
    }



}

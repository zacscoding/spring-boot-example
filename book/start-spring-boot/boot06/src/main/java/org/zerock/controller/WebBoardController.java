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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.WebBoard;
import org.zerock.persistence.CustomCrudRepository;
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
    @Autowired
    CustomCrudRepository customCrudRepository;

    @GetMapping("/list")
    public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
        Pageable page = vo.makePageable(0,"bno");

        Page<Object[]> result = customCrudRepository.getCustomPage(vo.getType(), vo.getKeyword(), page);

        log.info("## [list is called] page : " + page);
        log.info("## result : " + result);
        log.info("## TOTAL PAGE NUMBER : " + result.getTotalPages());

        model.addAttribute("result", new PageMaker(result));
    }

    /* == before reply cnt
    @GetMapping("/list")
    //@PageableDefault
    // public void list(@PageableDefault(direction = Sort.Direction.DESC, sort="bno", size=10, page=0) Pageable pageable) {
    public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
        Pageable page = vo.makePageable(0,"bno");

        Page<WebBoard> result = webBoardRepository.findAll(webBoardRepository.makePredicate(vo.getType(),vo.getKeyword()), page);

        log.info("## [list is called] page : " + page);
        log.info("## result : " + result);
        log.info("## TOTAL PAGE NUMBER : " + result.getTotalPages());

        model.addAttribute("result", new PageMaker(result));
    }
    */

    @GetMapping("/register")
    public void registerGET(@ModelAttribute("vo") WebBoard vo) {
        log.info("## register GET : " + vo);
    }

    @PostMapping("/register")
    public String registerPOST(@ModelAttribute("vo") WebBoard vo, RedirectAttributes rttr) {
        log.info("## register POST : " + vo);

        webBoardRepository.save(vo);
        rttr.addFlashAttribute("msg","success");

        return "redirect:/boards/list";
    }

    @GetMapping("/view")
    public void view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
        log.info("## Detail : " + bno);

        webBoardRepository.findById(bno).ifPresent(board -> {
            model.addAttribute("vo", board);
        });
    }

    @GetMapping("/modify")
    public void modify(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
        log.info("## modify GET: " + bno);
        webBoardRepository.findById(bno).ifPresent(board -> {
            model.addAttribute("vo", board);
        });
    }

    @PostMapping("/modify")
    public String modifyPost(WebBoard board, PageVO vo, RedirectAttributes rttr) {
        log.info("## modify Post : " + board);

        webBoardRepository.findById(board.getBno()).ifPresent(origin -> {
            origin.setTitle(board.getTitle());
            origin.setContent(board.getContent());

            webBoardRepository.save(origin);

            rttr.addFlashAttribute("msg", "success");
            rttr.addAttribute("bno", origin.getBno());
        });

        // 페이징 + 검색 결과로 이동
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/boards/view";
    }

    @PostMapping("/delete")
    public String delete(Long bno, PageVO vo, RedirectAttributes rttr) {
        log.info("## delete : " + bno);
        webBoardRepository.deleteById(bno);

        rttr.addFlashAttribute("msg", "success");
        // 기존 page 로 리다이렉트
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/boards/list";
    }



}

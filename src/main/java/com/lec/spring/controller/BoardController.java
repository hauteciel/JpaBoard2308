package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.domain.PostValidator;
import com.lec.spring.service.BoardService;
import com.lec.spring.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

// Controller layer
//  request 처리 ~ response
@Controller
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;

    @Autowired
    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    public BoardController(){
        System.out.println("BoardController() 생성");
    }

    @GetMapping("/write")
    public void write(){}

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files     // 첨부파일들
            , @ModelAttribute("post") Post post
            , Model model
        ){
        model.addAttribute("result", boardService.write(post, files));
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable long id, Model model){
        model.addAttribute("post", boardService.detail(id));
        model.addAttribute("conPath", U.getRequest().getContextPath());
        return "board/detail";
    }

    // 페이징 사용
    @GetMapping("/list")
    //public void list(Model model){
    public void list(Integer page, Model model){
//        model.addAttribute("list", boardService.list());
        boardService.list(page, model);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable long id, Model model){
        model.addAttribute("post", boardService.selectById(id));
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @ModelAttribute("post") Post post
            , @RequestParam Map<String, MultipartFile> files     // 새로 추가될 첨부파일들
            , Long[] delfile    // 삭제될 파일들
            , Model model){
        model.addAttribute("result", boardService.update(post, files, delfile));
        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(long id, Model model){
        model.addAttribute("result", boardService.deleteById(id));
        return "board/deleteOk";
    }

    // 이 컨트롤러 클래스의 handler 에서 폼 데이터를 바인딩 할때 검증하는 Validator 객체 지정
    @InitBinder
    public void initBinder(WebDataBinder binder){
        System.out.println("initBinder() 호출");
        binder.setValidator(new PostValidator());
    }

    // 페이징
    // pageRows 변경시 동작
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows){
        U.getSession().setAttribute("pageRows", pageRows);
        return "redirect:/board/list?page=" + page;
    }

} // end Controller











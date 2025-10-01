package kr.co.greengarden.controller.cs;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * 날짜 : 2025/09/26
 * 이름 : 박효빈
 * 내용 : QnaController  추가
 */
@Controller
public class QnaController {

    // 전체 리스트
    @GetMapping("/cs/qna/list")
    public String list(){
        return "cs/qna/list";
    }

    // 글보기
    @GetMapping("/cs/qna/view")
    public String view(){
        return "cs/qna/view";
    }

    // 글작성
    @GetMapping("/cs/qna/write")
    public String write(){
        return "cs/qna/write";
    }
}

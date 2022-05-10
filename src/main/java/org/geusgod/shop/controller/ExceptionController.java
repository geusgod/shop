package org.geusgod.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

//@Api("예외 컨트롤러")
@Controller
@RequestMapping("/exception")
public class ExceptionController {

//    @ApiOperation("예외 강제 발생")
    @GetMapping("/ex01")
    public String exceptionExample01() {
        try {
            throw new IOException();
        }catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("예외 처리 완료");

        return "exception/Ex01";
    }

//    @ApiOperation("예외 강제 발생")
    @GetMapping("/ex02")
    public String exceptionExample02() throws Exception {
        try {
            throw new IOException();
        }catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("예외 처리 완료");

        return "exception/Ex01";
    }
}

package com.app.kokonut.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    // 로그인 페이지
    @RequestMapping("loginUI")
    public String account_List(){
        return "/Login/LoginUI";
    }

}

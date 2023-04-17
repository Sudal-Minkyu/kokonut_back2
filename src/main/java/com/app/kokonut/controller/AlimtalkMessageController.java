package com.app.kokonut.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 알림톡 페이지 관련 화면 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/alimtalk")
public class AlimtalkMessageController {

    // 카카오톡 채널 관리 화면
    @RequestMapping(value = "/kakaoTalkChannelManagement")
//    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")})
    public void kakaoTalkChannelManagement(Model model){

        log.info("카카오 채널 관리 화면이동");
//        String userEmail = SecurityUtil.getCurrentUserEmail();

//        model.addAttribute("email", userEmail);
//        return "/Member/KakaoTalkChannelManagementUI";
    }

    // 알림톡 템플릿 관리 화면
    @RequestMapping(value = "/template/alimTalkTemplateManagement")
    public void alimTalkTemplateManagement(Model model){

        log.info("알림톡 템플릿 관리 화면");
//        return "/Member/BizMessage/AlimTalkTemplateManagementUI";
    }







}

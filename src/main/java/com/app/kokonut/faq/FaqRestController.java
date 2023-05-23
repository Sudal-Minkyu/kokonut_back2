package com.app.kokonut.faq;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.faq.dtos.FaqDetailDto;
import com.app.kokonut.faq.dtos.FaqSearchDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.data.web.PageableDefault;

@Api(tags = "")
@Validated
@RestController
@RequestMapping("/v2/api/Faq")
public class FaqRestController {
    // 기존 코코넛 SystemFaqController 컨트롤러 리팩토링
    // 기존 url : /system/faq , 변경 url : /api/Faq

    // 기존 코코넛 FaqController 컨트롤러 리팩토링
    // 기존 url : /faq, 변경 url : /api/Faq

    private final FaqService faqService;

    public FaqRestController(FaqService faqService) {
        this.faqService = faqService;
    }

    @ApiOperation(value="Faq 목록 조회", notes="" +
            "1. 토큰과 페이지 처리를 위한 값을 받는다." +
            "2. 자주 묻는 질문 목록을 조회한다.")
    @GetMapping(value = "/faqList") // -> 기존의 코코넛 호출 메서드명 : list - SystemFaqController, FaqController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> faqList(@RequestBody FaqSearchDto faqSearchDto, @PageableDefault Pageable pageable) {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        return faqService.faqList(null, faqSearchDto, pageable);
    }

    @ApiOperation(value="Faq 내용 조회", notes="" +
            "1. 토큰과 조회하고자 하는 자주묻는 질문 ID를 받는다." +
            "2. 자주 묻는 질문 내용을 조회한다.")
    @GetMapping(value = "/faqDetail/{faqId}") // -> 기존의 코코넛 호출 메서드명 : detailView - SystemFaqController, FaqController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> faqDetail(@PathVariable("faqId") Long faqId) {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        return faqService.faqDetail(null, faqId);
    }

    @ApiOperation(value="Faq 등록, 수정", notes="" +
            "1. 자주 묻는 질문 등록, 수정")
    @PostMapping(value = "/faqSave") // -> 기존의 코코넛 호출 메서드명 : save - SystemFaqController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> faqSave(@RequestBody FaqDetailDto faqDetailDto) {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return faqService.faqSave(null, email, faqDetailDto);
    }

    @ApiOperation(value="Faq 삭제", notes="" +
            "1. 토큰과 삭제하고자 하는 자주묻는 질문 ID를 받는다." +
            "2. 자주 묻는 질문을 삭제한다.")
    @PostMapping(value = "/faqDelete") // -> 기존의 코코넛 호출 메서드명 : delete - SystemFaqController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> faqDelete(@RequestParam(name="faqId") Long faqId) {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return faqService.faqDelete(null, email, faqId);
    }


}

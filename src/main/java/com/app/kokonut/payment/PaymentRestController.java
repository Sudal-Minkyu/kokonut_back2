package com.app.kokonut.payment;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : 구독관리 관련 RestController
 */
@Slf4j
@RequestMapping("/v2/api/Payment")
@RestController
public class PaymentRestController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentRestController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    // 자동결제 카드 신규등록(부트페이 빌링키등록 & 변경)
    @PostMapping(value = "/billingSave")
    @ApiOperation(value = "자동결제 카드 신규등록(부트페이 빌링키등록) 및 변경" , notes = "" +
            "1. 인증방식의 부트페이팝업의 정보를 입력하여 받은 receipt_id를 받는다." +
            "2. 받은 receipt_id를 저장한다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> billingSave(@RequestParam(value="payReceiptId", defaultValue = "") String payReceiptId) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return paymentService.billingSave(payReceiptId, jwtFilterDto);
    }

//    // 이번달 요금 결제
//    @PostMapping(value = "/existsByKnEmail")
//    @ApiOperation(value = "이메일 중복확인 버튼" , notes = "" +
//            "1. 이메일 중복확인을 한다." +
//            "2. 결과의 대해 false 또는 true를 보낸다.")
//    public ResponseEntity<Map<String,Object>> existsKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail) {
//        return paymentService.existsKnEmail(knEmail);
//    }
//
//    // 구독관리 리스트 호출
//    @GetMapping(value = "/numberSendKnEmail")
//    @ApiOperation(value = "이메일 인증번호 보내기 버튼" , notes = "" +
//            "1. 이메일 중복확인한 이메일의 대해 인증번호를 보낸다." +
//            "2. 번호를 레디스DB에 담는다. (유효기간은 3분)")
//    public ResponseEntity<Map<String,Object>> numberSendKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail) throws IOException {
//        return paymentService.numberSendKnEmail(knEmail);
//    }
//
//    // 구독해지하기(부트페이 빌링키 제거)
//    @GetMapping(value = "/numberCheckKnEmail")
//    @ApiOperation(value = "이메일 인증번호 보내기 검증" , notes = "" +
//            "1. 받은 인증번호를 받아 맞는지 확인한다.")
//    public ResponseEntity<Map<String,Object>> numberCheckKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail,
//                                                                 @RequestParam(value="ctNumber", defaultValue = "") String ctNumber) {
//        return paymentService.numberCheckKnEmail(knEmail, ctNumber);
//    }



}

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

    @PostMapping(value = "/billingPay")
    @ApiOperation(value = "요금정산 계산" , notes = "" +
            "1. 정산받을 요금을 받는다." +
            "2. 받은 요금의 값이 빈값이거나, 0원일 경우 에러를 던저준다." +
            "3. 결제API를 호출하여 결제하고 디비에 기록한다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> billingPay(@RequestParam(value="payAmount", defaultValue = "") String payAmount) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return paymentService.billingPay(payAmount, jwtFilterDto);
    }

    @PostMapping(value = "/billingDelete")
    @ApiOperation(value = "구독해지" , notes = "")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> billingDelete() throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return paymentService.billingDelete(jwtFilterDto);
    }

}

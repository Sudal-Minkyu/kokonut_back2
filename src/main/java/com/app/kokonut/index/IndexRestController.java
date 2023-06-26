package com.app.kokonut.index;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Woody
 * Date : 2023-06-22
 * Time :
 * Remark : 인덱스페이지 관련 RestController
 */
@Slf4j
@RequestMapping("/v2/api/Index")
@RestController
public class IndexRestController {

    private final IndexService indexService;

    @Autowired
    public IndexRestController(IndexService indexService){
        this.indexService = indexService;
    }

    @ApiOperation(value="나의 접속 현황(로그인현황) 최근일자로부터 5건을 가져온다.", notes="")
    @GetMapping(value = "/myLoginInfo")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> myLoginInfo() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.myLoginInfo(jwtFilterDto);
    }

    @ApiOperation(value="관리자 접속현황 데이터를 가져온다.", notes="")
    @GetMapping(value = "/adminConnectInfo")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> adminConnectInfo() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.adminConnectInfo(jwtFilterDto);
    }


    @ApiOperation(value="인덱스페이지에 표출할 개인정보 제공 건수를 가져온다.", notes="")
    @GetMapping(value = "/privacyOfferCount")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyOfferCount(@RequestParam(value="dateType", defaultValue = "") String dateType) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.privacyOfferCount(dateType, jwtFilterDto);
    }


    // 금일 개인정보 다운로드 건수


    // 나의접속현황 데이터 - 개인

    // 관리자접속현황 데이터 - 기업

    // 서드파티 연동 현황 - 기업

    // 구독관리 관련 - 기업 (디폴트값 이번달)

    // 개인정보현황 - 기업

    // 이메일발송 현황 건수 데이터 - 기업 (디폴트 오늘)




//
//    // 자동결제 카드 신규등록(부트페이 빌링키등록 & 변경)
//    @PostMapping(value = "/billingSave")
//    @ApiOperation(value = "자동결제 카드 신규등록(부트페이 빌링키등록) 및 변경" , notes = "" +
//            "1. 인증방식의 부트페이팝업의 정보를 입력하여 받은 receipt_id를 받는다." +
//            "2. 받은 receipt_id를 저장한다.")
//    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> billingSave(@RequestParam(value="payReceiptId", defaultValue = "") String payReceiptId) throws Exception {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return paymentService.billingSave(payReceiptId, jwtFilterDto);
//    }
//
//    @PostMapping(value = "/billingPay")
//    @ApiOperation(value = "요금정산 계산" , notes = "" +
//            "1. 정산받을 요금을 받는다." +
//            "2. 받은 요금의 값이 빈값이거나, 0원일 경우 에러를 던저준다." +
//            "3. 결제API를 호출하여 결제하고 디비에 기록한다.")
//    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> billingPay(@RequestParam(value="payAmount", defaultValue = "") String payAmount) throws Exception {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return paymentService.billingPay(payAmount, jwtFilterDto);
//    }
//
//    @PostMapping(value = "/billingDelete")
//    @ApiOperation(value = "구독해지" , notes = "")
//    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> billingDelete() throws Exception {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return paymentService.billingDelete(jwtFilterDto);
//    }

}

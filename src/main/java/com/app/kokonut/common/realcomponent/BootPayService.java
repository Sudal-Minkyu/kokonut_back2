package com.app.kokonut.common.realcomponent;

import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSaveDto;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.Subscribe;
import kr.co.bootpay.model.request.SubscribePayload;
import kr.co.bootpay.model.request.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * @author Woody
 * Date : 2023-05-03
 * Remark : 부트페이 관련 유틸리티
 */
@Slf4j
@Service
public class BootPayService {

    @Value("${kokonut.bootpay.restKey}")
    public String restKey;

    @Value("${kokonut.bootpay.privateKey}")
    public String privateKey;

    @SuppressWarnings("unchecked")
    public CompanyPaymentSaveDto billingKeyCheck(String receiptId) throws Exception {
        log.info("billingKeyCheck 호출");

        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        CompanyPaymentSaveDto companyPaymentSaveDto = new CompanyPaymentSaveDto();
        try {
            HashMap<String, Object> res = bootpay.lookupBillingKey(receiptId);
            HashMap<String, Object> resSub = (HashMap<String, Object>) res.get("billing_data");
//            JSONObject json = new JSONObject(res);
//            System.out.printf("JSON: %s", json);
            if (res.get("error_code") == null) {

                log.info("빌링키 조회 성공");

                companyPaymentSaveDto.setCpiBillingKey(String.valueOf(res.get("billing_key")));
                companyPaymentSaveDto.setCpiBillingDate(OffsetDateTime.parse(String.valueOf(res.get("published_at"))).toLocalDateTime());
                companyPaymentSaveDto.setCpiBillingExpireDate(OffsetDateTime.parse(String.valueOf(res.get("billing_expire_at"))).toLocalDateTime());
                companyPaymentSaveDto.setCpiReceiptId(String.valueOf(res.get("receipt_id")));
                companyPaymentSaveDto.setCpiSubscriptionId(String.valueOf(res.get("subscription_id")));

                companyPaymentSaveDto.setCpiInfoCardName(String.valueOf(resSub.get("card_company")));
                companyPaymentSaveDto.setCpiInfoCardNo(String.valueOf(resSub.get("card_no")));
                companyPaymentSaveDto.setCpiInfoCardType(String.valueOf(resSub.get("card_type")));

            } else {
                log.error("빌링키 조회 실패");
            }

            return companyPaymentSaveDto;

        } catch (Exception e) {
            log.error("예외처리 : " + e);
            log.error("예외처리 메세지 : " + e.getMessage());

            return null;
        }
    }

    // 토큰발급(사용하지 않음)
    public void goGetToken() {
        log.info("토큰 발급받기 함수 실행!");
        try {
            Bootpay bootpay = new Bootpay(restKey, privateKey);
            HashMap<String, Object> res = bootpay.getAccessToken();
            if (res.get("error_code") == null) { //success
                log.info("토큰받기 성공: " + res);
            } else {
                log.info("토큰받기 실패: " + res);
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
        }
    }

    // 빌링키 발급(사용하지 않음)
    public void getBillingKey() throws Exception {
        log.info("빌링키 발급받기 함수 실행!");
        log.info("restKey : "+restKey);
        log.info("privateKey : "+privateKey);

        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        Subscribe subscribe = new Subscribe();
        subscribe.orderName = "정기결제 빌링키 발급 테스트";
        subscribe.subscriptionId = String.valueOf(System.currentTimeMillis() / 1000);
        subscribe.pg = "나이스페이";

        subscribe.cardNo = "5105545000809043"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardPw = "11"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireYear = "27"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireMonth = "11"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardIdentityNo = "3488101536"; //생년월일 또는 사업자 등록번호 (- 없이 입력)

        subscribe.user = new User();
        subscribe.user.username = "";
        subscribe.user.phone = "";

        try {
            HashMap<String, Object> res = bootpay.getBillingKey(subscribe);
//            JSONObject json =  new JSONObject(res);
//            System.out.printf( "JSON: %s", json);

            if(res.get("error_code") == null) {
                log.info("빌링키 발급 성공: " + res);
                log.info("빌링키 : " + res.get("billing_key"));
                log.info("빌링 조회키: " + res.get("receipt_id"));
            } else {
                log.info("빌링키 발급 실패: " + res);
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
        }
    }

    // 빌링키 삭제
    public void billingKeyDelete(String billingKey) throws Exception {
        log.info("빌링키 삭제하기 실행!");

        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        try {
            HashMap<String, Object> res = bootpay.destroyBillingKey(billingKey);
//            JSONObject json =  new JSONObject(res);
//            System.out.printf( "JSON: %s", json);
            if(res.get("error_code") == null) {
                log.info("빌링키 삭제 성공: " + res);
            } else {
                log.error("빌링키 삭제 실패: " + res);
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

    }

    // 부트페이 결제예약
    public String kokonutReservationPayment(String cpCode, Integer price, String orderName, String billingKey) throws Exception {
        log.info("100원 예약 결제하기 함수 실행!");
        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = billingKey; // 조회한 빌링키
        payload.orderName = orderName; // 자동결제 내용 -> 무슨무슨회사의 무엇무엇결제의 금액
        payload.price = price; // 결제할 금액
        payload.orderId = cpCode; // 고유 주문번호

        Date now = new Date();
        now.setTime(now.getTime() + 10 * 1000); //10초 뒤 결제


        // 카드연결시 -> 연결날부터 한달간 서비스 무료사용
        // 이후 30일(한달)이 지나면 다음 날짜로부터 이번달말일까지 일할계산하며,
        // 다음달 1일부터 막일까지 한달간 정기결제함

        // 결제금액은 막일당일 현재 개인정보수를 통해 금액이 결정되며 결제예약 서비스를 호출함
        //   -> 1. 다음날 결제 자동진행(환불요청시 결제환불도 할 수 있음)
        //   -> 2. 결제테이블(내용)은 구독관리페이지에 안내하기로함 어디에 표시할진 아직 안정함
        // 호출 동시에 금일 결제될 금액과 내용과 함께 이메일알림을 보냄.(첫 결제시에도 동일)

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss XXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        payload.reserveExecuteAt = sdf.format(now); // 결제 승인 시점

        String result = "";
        try {
            HashMap<String, Object> res = bootpay.reserveSubscribe(payload);
            if(res.get("error_code") == null) { //success
                log.info("예약 결제 성공: " + res);
            } else {
                log.error("예약 결제 실패: " + res);
            }

            return result;
        } catch (Exception e) {
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());

            return null;
        }

    }

    // 부트페이 결제하기
    public void kokonutPayment() throws Exception {
        log.info("100원 결제하기 함수 실행!");

        Bootpay bootpay = new Bootpay(restKey, privateKey);

        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "645206669f326b001fcb86e8";
        payload.orderName = "100원 결제 코코넛 테스트";
        payload.price = 100;
        payload.user = new User();
        payload.user.phone = "01012345678";
        payload.orderId = String.valueOf(System.currentTimeMillis() / 1000);

        try {
            HashMap<String, Object> res = bootpay.requestSubscribe(payload);
//            JSONObject json =  new JSONObject(res);
//            System.out.printf( "JSON: %s", json);

            if(res.get("error_code") == null) { //success
                System.out.println("결제 성공 : " + res);
            } else {
                System.out.println("결제 실패 : " + res);
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

    }

    // 부트페이 결제취소
    public void goCancel(String receipt_id) {
        Bootpay bootpay = new Bootpay(restKey, privateKey);

        Cancel cancel = new Cancel();
        cancel.receiptId = "6100e77a019943003650f4d5";
        cancel.cancelUsername = "관리자";
        cancel.cancelMessage = "테스트 결제";
        //cancel.price = 1000.0; //부분취소 요청시
        //cancel.cancelId = "12342134"; //부분취소 요청시, 중복 부분취소 요청하는 실수를 방지하고자 할때 지정

        //RefundData refund = new RefundData(); // 가상계좌 환불 요청시, 단 CMS 특약이 되어있어야만 환불요청이 가능하다.
        //refund.account = "675601012341234"; //환불계좌
        //refund.accountholder = "홍길동"; //환불계좌주
        //refund.bankcode = BankCode.getCode("국민은행");//은행코드
        //cancel.refund = refund;

        try {
            HashMap<String, Object> res = bootpay.receiptCancel(cancel);
            if(res.get("error_code") == null) {
                log.info("결제취소 성공: " + res);
            } else {
                log.error("결제취소 실패: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




























































}

package com.app.kokonut.common.realcomponent;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Subscribe;
import kr.co.bootpay.model.request.SubscribePayload;
import kr.co.bootpay.model.request.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
public class BootPayUtil {

    @Value("${kokonut.bootpay.restKey}")
    public String restKey;

    @Value("${kokonut.bootpay.privateKey}")
    public String privateKey;
    
    private Bootpay bootpay;
    
    public void main(String[] args) throws Exception {
//        bootpay = new Bootpay(restKey, privateKey);
//        goGetToken(); // 토큰 발급받기 함수
//        getBillingKey(); // 빌링키 발급받기 함수
//        goPay(); // 100원 결제하기 함수
//        kokonutReservationPayment(); // 100원 예약 결제하기 함수
//        billingKeyCheck(); // 빌링키 조회하기 함수
//        billingKeyDelete(); // 빌링키 삭제하기 함수
    }


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
            e.printStackTrace();
        }
    }

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

        // 회사 빌링키 : 64520f1e755e27001fe1993c
        // 회사 빌링키 조회키 : 64520f1d755e27001fe19939
        subscribe.cardNo = "5105545000809043"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardPw = "11"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireYear = "27"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireMonth = "11"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardIdentityNo = "3488101536"; //생년월일 또는 사업자 등록번호 (- 없이 입력)

//        subscribe.cardNo = "6253208233287887"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardPw = "14"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardExpireYear = "25"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardExpireMonth = "08"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardIdentityNo = "940716"; //생년월일 또는 사업자 등록번호 (- 없이 입력)

        // 조프리 빌링키 :
        // 회사 빌링키 조회키 :
//        subscribe.cardNo = "5570422360164078"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardPw = "15"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardExpireYear = "25"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardExpireMonth = "03"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
//        subscribe.cardIdentityNo = "860823"; //생년월일 또는 사업자 등록번호 (- 없이 입력)

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }

    }

    public void kokonutReservationPayment() throws Exception {
        log.info("100원 예약 결제하기 함수 실행!");
        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "64520f1e755e27001fe1993c";
        payload.orderName = "100월 예약 결제 코코넛 테스트";
        payload.price = 1000;
        payload.orderId = String.valueOf(System.currentTimeMillis() / 1000);

        Date now = new Date();
        now.setTime(now.getTime() + 10 * 1000); //10초 뒤 결제

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss XXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        payload.reserveExecuteAt = sdf.format(now); // 결제 승인 시점

        try {
            HashMap<String, Object> res = bootpay.reserveSubscribe(payload);
            if(res.get("error_code") == null) { //success
                System.out.println("예약 결제 성공: " + res);
            } else {
                System.out.println("예약 결제 실패: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void billingKeyCheck() throws Exception {
        log.info("빌링키 조회하기 실행!");
        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        String receiptId = "64520ca4755e27001ee19a5b";
        try {
            HashMap<String, Object> res = bootpay.lookupBillingKey(receiptId);
//            JSONObject json =  new JSONObject(res);
//            System.out.printf( "JSON: %s", json);
            if(res.get("error_code") == null) {
                System.out.println("빌링키 조회 성공 : " + res.get("billing_key"));
            } else {
                System.out.println("빌링키 조회 실패 : " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void billingKeyDelete() throws Exception {
        log.info("빌링키 삭제하기 실행!");
        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        String billingKey = "64520ca5755e27001ee19a5e";
        try {
            HashMap<String, Object> res = bootpay.destroyBillingKey(billingKey);
//            JSONObject json =  new JSONObject(res);
//            System.out.printf( "JSON: %s", json);
            if(res.get("error_code") == null) {
                System.out.println("빌링키 삭제 성공: " + res);
            } else {
                System.out.println("빌링키 삭제 실패: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

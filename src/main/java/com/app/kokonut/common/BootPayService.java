package com.app.kokonut.common;

import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSaveDto;
import com.app.kokonut.payment.dtos.PaymentReservationResultDto;
import com.app.kokonut.payment.dtos.PaymentReservationSearchDto;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.SubscribePayload;
import kr.co.bootpay.model.request.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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

    // 빌링키 및 정보 조회
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

    // 빌링키 삭제
    public boolean billingKeyDelete(String billingKey) throws Exception {
        log.info("빌링키 삭제하기 실행!");

        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        try {
            HashMap<String, Object> res = bootpay.destroyBillingKey(billingKey);
//            JSONObject json =  new JSONObject(res);
//            System.out.printf( "JSON: %s", json);
            if(res.get("error_code") == null) {
                log.info("빌링키 삭제 성공: " + res);
                return true;
            } else {
                log.error("빌링키 삭제 실패: " + res);
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return false;

    }

    // 부트페이 결제예약
    public PaymentReservationResultDto kokonutReservationPayment(String cpCode, Integer price, String orderName, String billingKey, LocalDateTime localDateTime) throws Exception {
        log.info("월 사용료 결제예약하기 함수 실행!");
        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        PaymentReservationResultDto paymentReservationResultDto = new PaymentReservationResultDto();

        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = billingKey; // 조회한 빌링키
        payload.orderName = orderName; // 자동결제 내용 -> 무슨무슨회사의 무엇무엇결제의 금액
        payload.price = price; // 결제할 금액
        payload.orderId = cpCode; // 고유 주문번호

        // 카드연결시 -> 연결날부터 한달간 서비스 무료사용
        // 이후 30일(한달)이 지나면 다음 날짜로부터 이번달말일까지 일할계산하며,
        // 다음달 1일부터 막일까지 한달간 정기결제함

        // 결제금액은 막일당일 현재 개인정보수를 통해 금액이 결정되며 결제예약 서비스를 호출함
        //   -> 1. 다음날 결제 자동진행(환불요청시 결제환불도 할 수 있음)
        //   -> 2. 결제테이블(내용)은 구독관리페이지에 안내하기로함 어디에 표시할진 아직 안정함
        // 호출 동시에 금일 결제될 금액과 내용과 함께 이메일알림을 보냄.(첫 결제시에도 동일)

        // LocalDateTime 컨버트
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        Date now = Date.from(instant);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss XXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        payload.reserveExecuteAt = sdf.format(now); // 결제 승인 시점

        try {
            HashMap<String, Object> res = bootpay.reserveSubscribe(payload);
            if(res.get("error_code") == null) { //success
                log.info("예약 결제 성공: " + res);
                paymentReservationResultDto.setPayReserveId(String.valueOf(res.get("reserve_id")));
                paymentReservationResultDto.setPayReserveExecuteDate(OffsetDateTime.parse(String.valueOf(res.get("reserve_execute_at"))).toLocalDateTime());
                return paymentReservationResultDto;
            } else {
                log.error("예약 결제 실패: " + res);
            }

        } catch (Exception e) {
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
        }

        return null;
    }

    // 부트페이 결제하기(요금정산)
    public String kokonutPayment(String billingKey, String orderId, Integer payAmount, String orderName, String knName, String knPhoneNumber) throws Exception {
        log.info("부트페이 결제하기 함수 실행!(요금정산)");

        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = billingKey;
        payload.orderName = orderName;
        payload.price = payAmount;
        payload.user = new User();
        payload.user.username = knName;
        payload.user.phone = knPhoneNumber;
        payload.orderId = orderId;

        try {
            HashMap<String, Object> res = bootpay.requestSubscribe(payload);
//            JSONObject json =  new JSONObject(res);
//            System.out.printf( "JSON: %s", json);

            if(res.get("error_code") == null) { //success
                System.out.println("결제 성공 : " + res);
                return String.valueOf(res.get("receipt_id"));
            } else {
                System.out.println("결제 실패 : " + res);
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return "";
    }

    // 예약결제 조회 삭제
    public PaymentReservationSearchDto kokonutReservationCheck(String payReserveId) throws Exception {
        log.info("예약결제 조회하기 실행!");

        Bootpay bootpay = new Bootpay(restKey, privateKey);
        bootpay.getAccessToken();

        PaymentReservationSearchDto paymentReservationSearchDto = new PaymentReservationSearchDto();
        try {
            HashMap<String, Object> res = bootpay.reserveSubscribeLookup(payReserveId);

            if(res.get("error_code") == null) {
                log.info("예약결제 조회 성공: " + res);

//                log.info("실행후 만들어진 값 : " + res.get("receipt_id"));
//                log.info("예약결제가 실행 시작된 시간 : " + res.get("reserve_started_at"));
//                log.info("예약결제가 실행이 완료된 시간 : " + res.get("reserve_finished_at"));
//                log.info("예약결제 상태 : " + res.get("status"));
//                log.info("결제 이후 Webhook을 전달한 Feedback URL : " + res.get("feedback_url"));
                int status = Integer.parseInt(String.valueOf(res.get("status")));
                if(status == 3) {
                    return null;
                } else {
                    paymentReservationSearchDto.setStatus(status);
                    if(status != 0) {
                        // -1(예약결제실패) 또는 1(예약결제 완료)일때만 통과
                        if(res.get("receipt_id") != null) {
                            paymentReservationSearchDto.setPayReceiptid(String.valueOf(res.get("receipt_id")));
                        }
                        if(res.get("reserve_started_at") != null) {
                            paymentReservationSearchDto.setPayReserveStartedDate(OffsetDateTime.parse(String.valueOf(res.get("reserve_started_at"))).toLocalDateTime());
                        }
                        if(res.get("reserve_finished_at") != null) {
                            paymentReservationSearchDto.setPayReserveFinishedDate(OffsetDateTime.parse(String.valueOf(res.get("reserve_finished_at"))).toLocalDateTime());
                        }
                    }
                    return paymentReservationSearchDto;
                }

            } else {
                log.error("예약결제 조회 실패: " + res);
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return null;
    }

}
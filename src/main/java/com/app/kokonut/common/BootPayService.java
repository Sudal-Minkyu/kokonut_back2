package com.app.kokonut.common;

import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSaveDto;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.SubscribePayload;
import kr.co.bootpay.model.request.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;

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

}
